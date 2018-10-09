package com.bitmovin.analytics.data;

public class DrmPerformanceInfo {
    private boolean drmUsed;
    private String drmType;
    private long drmLoadTime;

    public boolean isDrmUsed() {
        return drmUsed;
    }

    public void setDrmUsed(boolean drmUsed) {
        this.drmUsed = drmUsed;
    }

    public String getDrmType() {
        return drmType;
    }

    public void setDrmType(String drmType) {
        this.drmType = drmType;
    }

    public long getDrmLoadTime() {
        return drmLoadTime;
    }

    public void setDrmLoadTime(long drmLoadTime) {
        this.drmLoadTime = drmLoadTime;
    }
}
