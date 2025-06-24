package xyz.samsami.blokey_land.common.util;

public class StringUtil extends ch.qos.logback.core.util.StringUtil {
    public static boolean anyNotNullOrEmpty(String... strings) {
        for (String s : strings) {
            if (!isNullOrEmpty(s)) return true;
        }
        return false;
    }
}