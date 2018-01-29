package com.bitmovin.bitmovinanalyticscollector.data;

public interface IEventDataDispatcher {
    public void enable();

    public void disable();

    public void add(EventData data);

    public void clear();
}
