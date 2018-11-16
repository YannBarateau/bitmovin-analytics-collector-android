package com.bitmovin.analytics.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.List;

public class PermissionHelper {

    public static boolean checkPermission(Context ctx, String permission) {
        return ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkAllPermissions(Context ctx, List<String> permissions) {
        for (String permission : permissions)
            if (!checkPermission(ctx, permission))
                return false;
        return true;
    }

    public static boolean checkAllPermissions(Context ctx, String[] permissions) {
        for (String permission : permissions)
            if (!checkPermission(ctx, permission))
                return false;
        return true;
    }

    public static boolean checkAnyPermission(Context ctx, String[] permissions) {
        for (String permission : permissions)
            if (checkPermission(ctx, permission))
                return true;
        return false;
    }

    public static boolean checkCoarseLocationPermission(Context ctx)
    {
        return checkPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    public static boolean checkFineLocationPermission(Context ctx)
    {
        return checkPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static boolean checkReadPhoneStatePermission(Context ctx)
    {
        return checkPermission(ctx, Manifest.permission.READ_PHONE_STATE);
    }

    public static boolean checkAccessNetworkStatePermission(Context ctx)
    {
        return checkPermission(ctx, Manifest.permission.ACCESS_NETWORK_STATE);
    }

}