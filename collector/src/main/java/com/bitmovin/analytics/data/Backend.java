package com.bitmovin.analytics.data;

public interface Backend {
    void send(EventData eventData);
    void sendAd(AdEventData eventData);
}
