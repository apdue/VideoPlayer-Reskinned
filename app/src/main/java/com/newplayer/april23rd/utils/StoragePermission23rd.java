package com.newplayer.april23rd.utils;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class StoragePermission23rd {
    public static int perRequest = 21;
    private static String[] getStoragePermissionList() {
        String[] PERMISSIONS_REQUIRED;

        if (Build.VERSION.SDK_INT >= 33) {
            PERMISSIONS_REQUIRED = new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
            };
        } else {
            PERMISSIONS_REQUIRED = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        }
        return PERMISSIONS_REQUIRED;
    }

    public static boolean isAllStoragePermissionsGranted(Context context) {
        for (String permission : getStoragePermissionList()) {
            if (ContextCompat.checkSelfPermission(context, permission) != PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestAllStoragePermissions(Activity activity, int requestCode) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String permission : getStoragePermissionList()) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PERMISSION_GRANTED) {
                arrayList.add(permission);
            }
        }
        String[] strArr = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            strArr[i] = (String) arrayList.get(i);
        }
        if (strArr.length > 0) {
            ActivityCompat.requestPermissions(activity, strArr, requestCode);
        }
    }

}
