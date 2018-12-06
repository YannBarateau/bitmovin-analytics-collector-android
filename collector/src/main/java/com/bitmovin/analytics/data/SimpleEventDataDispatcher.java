package com.bitmovin.analytics.data;

import android.util.Log;

import com.bitmovin.analytics.BitmovinAnalyticsConfig;
import com.bitmovin.analytics.utils.DataSerializer;
import com.bitmovin.analytics.utils.HttpClient;
import com.bitmovin.analytics.utils.LicenseCall;
import com.bitmovin.analytics.utils.LicenseCallback;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleEventDataDispatcher implements IEventDataDispatcher, LicenseCallback {
    private static final String TAG = "BitmovinAnalytics";

    private Queue<EventData> data;
    private HttpClient httpClient;
    private boolean enabled = false;
    private BitmovinAnalyticsConfig config;
    private LicenseCallback callback;

    public SimpleEventDataDispatcher(BitmovinAnalyticsConfig config, LicenseCallback callback) {
        this.data = new ConcurrentLinkedQueue<>();
        this.httpClient = new HttpClient(config.getContext(), BitmovinAnalyticsConfig.analyticsUrl);
        this.config = config;
        this.callback = callback;
    }

    @Override
    synchronized public void authenticationCompleted(boolean success) {
        if (success) {
            Log.d(TAG, String.format("Authentication completed successfully - flushing %d samples to Server", data.size()));
            enabled = true;
            Iterator<EventData> it = data.iterator();
            while (it.hasNext()) {
                EventData eventData = it.next();
                this.httpClient.post(DataSerializer.serialize(eventData), null);
                it.remove();
            }
        }

        if(callback != null) {
            callback.authenticationCompleted(success);
        }
    }

    @Override
    public void enable() {
        LicenseCall licenseCall = new LicenseCall(config);
        licenseCall.authenticate(this);
    }

    @Override
    public void disable() {
        Log.d(TAG, "Disabling EventDataDispatcher");
        this.data.clear();
        this.enabled = false;
    }

    @Override
    public void add(EventData eventData) {
        if (enabled) {
            send(eventData);
        } else {
            this.data.add(eventData);
        }
    }

    private void send(EventData eventData) {
        Log.d(TAG, String.format("Sending out sample with videoBitrate: %s", eventData.getVideoBitrate()));
        this.httpClient.post(DataSerializer.serialize(eventData), null);
    }

    @Override
    public void clear() {
        this.data.clear();
    }

}
