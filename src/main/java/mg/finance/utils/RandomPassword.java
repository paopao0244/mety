package mg.finance.utils;

// Java program generate a random AlphaNumeric String
// using Math.random() method

public class RandomPassword {

    // function to generate a random string of length n
    public static String getAlphaNumericString(int n)
    {

    // chose a Character random from this String
//        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//                + "0123456789"
//                + "abcdefghijklmnopqrstuvxyz";

        String AlphaNumericString = "0123456789";

    // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

    // generate a random number between
    // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

    // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
