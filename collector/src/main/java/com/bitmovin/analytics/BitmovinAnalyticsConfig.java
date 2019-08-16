package com.bitmovin.analytics;

import android.os.Parcel;
import android.os.Parcelable;

import com.bitmovin.analytics.enums.CDNProvider;
import com.bitmovin.analytics.enums.PlayerType;

public class BitmovinAnalyticsConfig implements Parcelable {
    private String analyticsUrl = "https://analytics-ingress-global.bitmovin.com/analytics";
    private String cdnProvider;
    private String customData1;
    private String customData2;
    private String customData3;
    private String customData4;
    private String customData5;
    private String customUserId;
    private String experimentName;
    private int heartbeatInterval = 59700;
    private String licenseUrl = "https://analytics-ingress-global.bitmovin.com/licensing";
    private String key;
    private String title;
    private String path;
    private String playerKey;
    private PlayerType playerType;
    private String videoId;

    public static final Creator<BitmovinAnalyticsConfig> CREATOR = new Creator<BitmovinAnalyticsConfig>() {
        @Override
        public BitmovinAnalyticsConfig createFromParcel(Parcel in) {
            return new BitmovinAnalyticsConfig(in);
        }

        @Override
        public BitmovinAnalyticsConfig[] newArray(int size) {
            return new BitmovinAnalyticsConfig[size];
        }
    };


    public BitmovinAnalyticsConfig(String key) {
        this.key = key;
        this.playerKey = "";
    }

    public BitmovinAnalyticsConfig(String key, String playerKey) {
        this.key = key;
        this.playerKey = playerKey;
    }

    protected BitmovinAnalyticsConfig(Parcel in) {
        analyticsUrl = in.readString();
        cdnProvider = in.readString();
        customData1 = in.readString();
        customData2 = in.readString();
        customData3 = in.readString();
        customData4 = in.readString();
        customData5 = in.readString();
        customUserId = in.readString();
        experimentName = in.readString();
        heartbeatInterval = in.readInt();
        licenseUrl = in.readString();
        key = in.readString();
        title = in.readString();
        path = in.readString();
        playerKey = in.readString();
        playerType = in.readParcelable(PlayerType.class.getClassLoader());
        videoId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(analyticsUrl);
        dest.writeString(cdnProvider);
        dest.writeString(customData1);
        dest.writeString(customData2);
        dest.writeString(customData3);
        dest.writeString(customData4);
        dest.writeString(customData5);
        dest.writeString(customUserId);
        dest.writeString(experimentName);
        dest.writeInt(heartbeatInterval);
        dest.writeString(licenseUrl);
        dest.writeString(key);
        dest.writeString(title);
        dest.writeString(path);
        dest.writeString(playerKey);
        dest.writeParcelable(playerType, flags);
        dest.writeString(videoId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getAnalyticsUrl() {
        return analyticsUrl;
    }

    public String getKey() {
        return key;
    }

    public String getPlayerKey() {
        return playerKey;
    }

    public String getCdnProvider() {
        return cdnProvider;
    }

    /**
     * CDN Provider used to play out Content
     *
     * @param cdnProvider {@link CDNProvider}
     */
    public void setCdnProvider(String cdnProvider) {
        this.cdnProvider = cdnProvider;
    }

    public String getVideoId() {
        return videoId;
    }

    /**
     * ID of the Video in the Customer System
     *
     * @param videoId
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getCustomUserId() {
        return customUserId;
    }

    /**
     * User-ID in the Customer System
     *
     * @param customUserId
     */
    public void setCustomUserId(String customUserId) {
        this.customUserId = customUserId;
    }

    public String getCustomData1() {
        return customData1;
    }

    /**
     * Optional free-form data
     *
     * @param customData1
     */
    public void setCustomData1(String customData1) {
        this.customData1 = customData1;
    }

    public String getCustomData2() {
        return customData2;
    }

    /**
     * Optional free-form data
     *
     * @param customData2
     */
    public void setCustomData2(String customData2) {
        this.customData2 = customData2;
    }

    public String getCustomData3() {
        return customData3;
    }

    /**
     * Optional free-form data
     *
     * @param customData3
     */
    public void setCustomData3(String customData3) {
        this.customData3 = customData3;
    }

    public String getCustomData4() {
        return customData4;
    }

    /**
     * Optional free-form data
     *
     * @param customData4
     */
    public void setCustomData4(String customData4) {
        this.customData4 = customData4;
    }

    public String getCustomData5() {
        return customData5;
    }

    /**
     * Optional free-form data
     *
     * @param customData5
     */
    public void setCustomData5(String customData5) {
        this.customData5 = customData5;
    }

    public String getExperimentName() {
        return experimentName;
    }

    /**
     * Human readable title of the video asset currently playing
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Human readable title of the video asset currently playing
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * A/B Test Experiment Name
     */
    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    /**
     * PlayerType that the current video is being played back with.
     *
     * @param playerType {@link PlayerType}
     */
    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public String getPath() {
        return path;
    }

    /**
     * Breadcrumb path
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    /**
     * The frequency that heartbeats should be sent, in milliseconds
     *
     * @param heartbeatInterval
     */
    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }
}
