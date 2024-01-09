package mg.finance.apiv.security.configuration.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class JwtGenerator {
    private String secret;
    private Long duration;

    public String generateMainToken(HttpServletRequest request, Long currentMillis, String username, List<String> roles){
        Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
        String issuerServer = request.getServerName().equals("127.0.0.1") ?
                "http://" + request.getServerName() + ":8081/login"
                :
                "http://" + request.getServerName() + ":" + request.getServerPort() + "/api-solde/login";
        String[] audience = {"rohi"};
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(currentMillis + this.duration))
                .withIssuer(issuerServer)
                .withAudience(audience)
                .withClaim("roles", roles)
//                .withClaim("publicKey", randomKey)
                .sign(algorithm);
    }

    public void isTokenContentSafe(HttpServletRequest request, String token) throws Exception {
        DecodedJWT decodedTokenNonVerified = JWT.decode(token);
        String issuer = decodedTokenNonVerified.getIssuer();
        List<String> audience = decodedTokenNonVerified.getAudience();

        if(audience == null || audience.size() != 1 || !audience.contains("rohi"))  throw new Exception("Token invalide");

        String decodedTokenNonVerifiedAlgorithm = decodedTokenNonVerified.getAlgorithm();
        if(!decodedTokenNonVerifiedAlgorithm.equals("HS256")) throw new Exception("Token invalide");

        String issuerServer = request.getServerName().equals("127.0.0.1") ?
                "http://" + request.getServerName() + ":8081/login"
                :
                "http://" + request.getServerName() + ":" + request.getServerPort() + "/api-solde/login";
        if(issuer == null || !issuer.equals(issuerServer)) throw new Exception("Token invalide");
    }
}
