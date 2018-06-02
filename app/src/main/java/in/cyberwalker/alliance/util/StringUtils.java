package in.cyberwalker.alliance.util;

import android.text.TextUtils;

public class StringUtils {
    public static boolean isNull(String txt) {
        return TextUtils.isEmpty(txt) || txt.equalsIgnoreCase("null");
    }
}
