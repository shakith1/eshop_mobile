package lk.oxo.eshop.util;

public class Validation {
    public static boolean checkEmail(String email){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    public static boolean checkMobile(String mobile){
        String mobilePattern = "^07(0|1|2|4|5|6|7|8)[0-9]{7}$";
        return mobile.matches(mobilePattern);
    }
}
