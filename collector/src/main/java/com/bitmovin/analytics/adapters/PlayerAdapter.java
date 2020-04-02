package com.bitmovin.analytics.adapters;

import com.bitmovin.analytics.data.DRMInformation;
import com.bitmovin.analytics.data.EventData;

import org.jetbrains.annotations.NotNull;

public interface PlayerAdapter {
    EventData createEventData();

    void release();
    long getPosition();
    @NotNull
    DRMInformation getDRMInformation();
}
