package in.cyberwalker.alliance.util;

import android.os.Build;

public class AppUtils {

    public static final int DAILY_NOTIFICATION_TIME = 9;

    public static boolean isGreaterAndEqual23() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
