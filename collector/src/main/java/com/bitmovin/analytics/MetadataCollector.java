package com.bitmovin.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import com.bitmovin.analytics.utils.PermissionHelper;


/**
 * A metadata collection module which reads network and hardware related information from the Android API.
 */
public class MetadataCollector {

  private static final String TAG = "MetadataCollector";

  /** Set to true if location information should be included */
  public final static boolean INCLUDE_LOCATION = true;

  /** Set to true if last signal information should be included */
  public final static boolean INCLUDE_LAST_SIGNAL_ITEM = true;

  /** Returned by getNetwork() if Wifi */
  public static final int NETWORK_WIFI = 99;

  /** Returned by getNetwork() if Ethernet */
  public static final int NETWORK_ETHERNET = 106;

  /** Returned by getNetwork() if Bluetooth */
  public static final int NETWORK_BLUETOOTH = 107;

  private static final String PLATTFORM_NAME = "Android";
  private static final int ACCEPT_WIFI_RSSI_MIN = -113;
  public static final int SIGNAL_TYPE_NO_SIGNAL = 0;
  public static final int SIGNAL_TYPE_MOBILE = 1;
  public static final int SIGNAL_TYPE_RSRP = 2;
  public static final int SIGNAL_TYPE_WLAN = 3;

  private ConnectivityManager connectivityManager = null;
  private TelephonyManager telephonyManager = null;
  private PhoneStateListener phoneStateListener = null;
  private WifiManager wifiManager = null;

  // Handlers and Receivers for phone and network state
  private BroadcastReceiver networkReceiver;
  private LocationManager locationManager = null;

  private Location lastLocation;

  private String OS_VERSION;
  private String API_LEVEL;
  private String DEVICE;
  private String MODEL;
  private String PRODUCT;

  private String NETWORK_TYPE;
  private String IS_ROAMING;

  private Context context = null;

  private static boolean haveCoarseLocationPermission;
  private static boolean haveFineLocationPermission;
  private static boolean haveReadPhoneStatePermission;
  private static boolean haveAccessNetworkStatePermission;

  /**
   * MetadataCollector
   *
   */

  public MetadataCollector() {
  public MetadataCollector(Context context) {
    this.context = context;

    haveCoarseLocationPermission = PermissionHelper.checkCoarseLocationPermission(context);
    haveFineLocationPermission = PermissionHelper.checkFineLocationPermission(context);
    haveReadPhoneStatePermission = PermissionHelper.checkReadPhoneStatePermission(context);
    haveAccessNetworkStatePermission = PermissionHelper.checkAccessNetworkStatePermission(context);

  }

  public void setHardwareMetadata() {
    OS_VERSION = android.os.Build.VERSION.RELEASE + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
    API_LEVEL = String.valueOf(android.os.Build.VERSION.SDK_INT);
    DEVICE = android.os.Build.DEVICE;
    MODEL = android.os.Build.MODEL;
    PRODUCT = android.os.Build.PRODUCT;
  }

  public void setNetworkMetadata() {
    NETWORK_TYPE = String.valueOf(getNetwork());
    IS_ROAMING = String.valueOf(getRoaming());
  }

  public int getNetwork()
  {
    int result = TelephonyManager.NETWORK_TYPE_UNKNOWN;

    if (connectivityManager != null)
    {
      final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
      if (activeNetworkInfo != null)
      {
        final int type = activeNetworkInfo.getType();
        switch (type)
        {
          case ConnectivityManager.TYPE_WIFI:
            result = NETWORK_WIFI;
            break;

          case ConnectivityManager.TYPE_BLUETOOTH:
            result = NETWORK_BLUETOOTH;
            break;

          case ConnectivityManager.TYPE_ETHERNET:
            result = NETWORK_ETHERNET;
            break;

          case ConnectivityManager.TYPE_MOBILE:
          case ConnectivityManager.TYPE_MOBILE_DUN:
          case ConnectivityManager.TYPE_MOBILE_HIPRI:
          case ConnectivityManager.TYPE_MOBILE_MMS:
          case ConnectivityManager.TYPE_MOBILE_SUPL:
            result = telephonyManager.getNetworkType();
            break;
        }
      }
    }
    return result;
  }

  public boolean getRoaming()
  {
    boolean result = false;

    if (connectivityManager != null)
    {
      final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
      if (activeNetworkInfo != null)
        result = activeNetworkInfo.isRoaming();
    }
    return result;
  }
}
