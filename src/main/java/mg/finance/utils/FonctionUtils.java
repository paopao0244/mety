package mg.finance.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class FonctionUtils {
    public static List<String> moisList = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
    public static String toStringBase(String texte) {
        if (texte == null) return null;
        String replace = texte.replace("'", "\\''");
        return "'" + replace + "'";
    }

    public static String toStringLocalDateBase(LocalDate date) {
        if (date == null) return null;
        return "TO_DATE('" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "', 'DD/MM/RRRR')";
    }

    public static String traiterString(String texte){
        if(texte == null || texte.trim().equals("")) return "";
        String texteTraite = texte.trim().toLowerCase();
        texteTraite = texteTraite.replaceAll(";","");
        texteTraite = texteTraite.replaceAll("=","");
        texteTraite = texteTraite.replaceAll("select ","");
        texteTraite = texteTraite.replaceAll("where ","");
        texteTraite = texteTraite.replaceAll("group by ","");
        texteTraite = texteTraite.replaceAll("drop ","");
        texteTraite = texteTraite.replaceAll("union ","");
        texteTraite = texteTraite.replaceAll("update ","");
        texteTraite = texteTraite.replaceAll("delete ","");
        texteTraite = texteTraite.replaceAll("from ","");
        return texteTraite;
    }

    public static boolean isStringSafe(String texte){

        if(texte == null) return true;
        texte = texte.toLowerCase().trim();
        if(texte.equals("")) return true;

        if(texte.contains(";")) return false;
        if(texte.contains("=")) return false;
        if(texte.contains("select ")) return false;
        if(texte.contains("where ")) return false;
        if(texte.contains("group by ")) return false;
        if(texte.contains("drop ")) return false;
        if(texte.contains("union ")) return false;
        if(texte.contains("update ")) return false;
        if(texte.contains("delete ")) return false;

        return !texte.contains("from ");
    }
}
