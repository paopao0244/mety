package mg.finance.apiv.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mg.finance.apiv.security.configuration.utils.Credentials;
import mg.finance.apiv.security.configuration.utils.JwtGenerator;
import mg.finance.apiv.security.utilisateur.UtilisateurAPIService;
import mg.finance.apiv.security.utilisateur.entity.UtilisateurAPI;
import mg.finance.utils.ErrorResponse;
import mg.finance.utils.FonctionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final UtilisateurAPIService utilisateurAPIService;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //JSON authentication
        if(HttpMethod.OPTIONS.matches(request.getMethod())){
            return null;
        }
        if(HttpMethod.POST.matches(request.getMethod())){
            String username;
            String password;
            ObjectMapper mapper = new ObjectMapper();
            Credentials credential = mapper.readValue(request.getInputStream(),Credentials.class);

            username = credential.getUsername();
            password = credential.getPassword();
            if(username == null || username.trim().equals("") || !FonctionUtils.isStringSafe(username)){
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                new ObjectMapper().writeValue(response.getOutputStream(), new ErrorResponse("Accès refusé"));
                return null;
            }
            UtilisateurAPI utilisateurByUsername = utilisateurAPIService.findUtilisateurByUsername(username);
            if(utilisateurByUsername == null){
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                new ObjectMapper().writeValue(response.getOutputStream(), new ErrorResponse("Le login n'existe pas"));
                return null;
            }else if(!utilisateurByUsername.getIsActive()){
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                new ObjectMapper().writeValue(response.getOutputStream(), new ErrorResponse("L'utilisateur est vérouillé : Veuillez contacter l'administrateur"));
                return null;
            }else{
                try{
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                    Authentication authenticate = authenticationManager.authenticate(authenticationToken);
                    if(utilisateurByUsername.getTentativeConnexion()!=0) utilisateurAPIService.reinitialiserTentativeConnexion(utilisateurByUsername.getId());
                    return authenticate;
                }catch (Exception e){
                    if((utilisateurByUsername.getTentativeConnexion()+1) < 5){
                        utilisateurAPIService.ajouterTentativeConnexion(utilisateurByUsername.getId());
                    }else{
                        utilisateurAPIService.ajouterTentativeConnexion(utilisateurByUsername.getId());
                        utilisateurAPIService.desactiverUtilisateurTropDeTentative(utilisateurByUsername.getId());
                    }
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    new ObjectMapper().writeValue(response.getOutputStream(), new ErrorResponse("Le mot de passe est erroné"));
                    return null;
                }
            }
        }else{
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        try{
            User user = (User) authentication.getPrincipal();
            Map<String, Object> tokens = new HashMap<>();
            List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            long currentTimeMillis = System.currentTimeMillis();
            tokens.put("access_token", jwtGenerator.generateMainToken(request, currentTimeMillis, user.getUsername(), roles));
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
