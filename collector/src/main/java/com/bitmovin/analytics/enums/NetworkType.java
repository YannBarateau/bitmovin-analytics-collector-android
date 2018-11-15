package com.bitmovin.analytics.enums;

public enum NetworkType {
    LAN("LAN", "Fixed"),
    ETHERNET("Ethernet", "Fixed"),
    WLAN("WiFi", "WiFi"),
    _1xRTT("1xRTT","Cellular-2G"),
    _2G3G("2G/3G", "Cellular"),
    _3G4G("3G/4G", "Cellular"),
    _2G4G("2G/4G", "Cellular"),
    _2G3G4G("2G/3G/4G", "Cellular"),
    CELLULAR_ANY("Cellular","Cellular"),
    GSM("GSM","Cellular-2G"),
    EDGE("EDGE","Cellular-2G"),
    UMTS("UMTS","Cellular-3G"),
    CDMA("CDMA","Cellular-2G"),
    EVDO_0("EVDO_0","Cellular-2G"),
    EVDO_A("EVDO_A","Cellular-2G"),
    HSDPA("HSDPA","Cellular-3G"),
    HSUPA("HSUPA","Cellular-3G"),
    HSPA("HSPA","Cellular-3G"),
    IDEN("IDEN","Cellular-2G"),
    EVDO_B("EVDO_B","Cellular-2G"),
    LTE("LTE","Cellular-4G"),
    EHRPD("EHRPD","Cellular-2G"),
    HSPA_PLUS("HSPA+","Cellular-3G"),
    BLUETOOTH("Bluetooth", "Other"),
    CLI("CLI", "Other"),
    UNKNOWN("Unknown", "Other");

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
