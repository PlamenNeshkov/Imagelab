package hacksoft.io.imagelab.permissions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {
    public static void request(Activity activity, String permission, int requestCode) {
        if (!isGranted(activity, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                ConfirmationDialog.newInstance(permission, requestCode)
                    .show(activity.getFragmentManager(), activity.getClass().getSimpleName());
            } else {
                String[] perms = {permission};
                ActivityCompat.requestPermissions(activity, perms, requestCode);
            }
        }
    }

    public static boolean isGranted(Activity activity, String permission) {
        int checkResult = ContextCompat.checkSelfPermission(activity, permission);
        return checkResult == PackageManager.PERMISSION_GRANTED;
    }
}
