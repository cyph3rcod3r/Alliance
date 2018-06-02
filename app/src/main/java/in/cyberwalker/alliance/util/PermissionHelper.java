package in.cyberwalker.alliance.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

public class PermissionHelper {

    public static void openSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (!hasPermission(context, permission)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean hasPermission(Context context, String permission) {
        if (context != null && permission != null && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    public static boolean shouldAskPermissionOrThrowToSettings(Activity activity, String[] permission) {
        for (String s : permission) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, s))
                return false;
        }
        return true;
    }
}
