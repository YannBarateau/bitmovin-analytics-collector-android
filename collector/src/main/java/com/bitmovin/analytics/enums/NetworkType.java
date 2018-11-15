package com.bitmovin.analytics.enums;

public enum NetworkType {
    _1xRTT("1xRTT","Cellular-2G"),
    _2G3G("2G/3G","Cellular"),
    _3G4G("3G/4G","Cellular"),
    _2G4G("2G/4G","Cellular"),
    _2G3G4G("2G/3G/4G","Cellular"),
    BLUETOOTH("Bluetooth","Other"),
    CDMA("CDMA","Cellular-2G"),
    CELLULAR_ANY("Cellular","Cellular"),
    CLI("CLI","Other"),
    EDGE("EDGE","Cellular-2G"),
    EHRPD("EHRPD","Cellular-2G"),
    ETHERNET("Ethernet","Fixed"),
    EVDO_0("EVDO_0","Cellular-2G"),
    EVDO_A("EVDO_A","Cellular-2G"),
    EVDO_B("EVDO_B","Cellular-2G"),
    GSM("GSM","Cellular-2G"),
    HSDPA("HSDPA","Cellular-3G"),
    HSPA("HSPA","Cellular-3G"),
    HSPA_PLUS("HSPA+","Cellular-3G"),
    HSUPA("HSUPA","Cellular-3G"),
    IDEN("IDEN","Cellular-2G"),
    LAN("LAN","Fixed"),
    LTE("LTE","Cellular-4G"),
    UMTS("UMTS","Cellular-3G"),
    UNKNOWN("Unknown","Other"),
    WLAN("WiFi","WiFi");

    protected final String networkId;
    protected final String networkFamily;

    NetworkType(String networkId, String family) {
        this.networkId = networkId;
        this.networkFamily = family;
    }

    NetworkType(String family) {
        this(family, family);
    }

    public String getNetworkId() {
        return networkId;
    }

    public String getNetworkFamily() {
        return networkFamily;
    }

    public static NetworkType getFamilyByNetworkId(String networkId) {
        for (NetworkType item : NetworkType.values()) {
            if (item.getNetworkId().equals(networkId)) {
                return item;
            }
        }
        return UNKNOWN;
    }
}
