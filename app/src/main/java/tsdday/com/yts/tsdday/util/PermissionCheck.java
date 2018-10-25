package tsdday.com.yts.tsdday.util;

import android.Manifest;
import android.content.Context;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

public class PermissionCheck {

    public static void imageCheck(Context context, PermissionListener permissionListener) {
        TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
}
