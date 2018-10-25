package tsdday.com.yts.tsdday.util;

public class NullFilter {
    public static String check(String text) {
        return text != null ? text : "";
    }

    public static Integer check(Integer integer) {
        return integer != null ? integer : 0;
    }

    public static boolean check(Boolean check) {
        return check != null ? check : false;
    }
}
