package com.bitmovin.analytics.stateMachines;

import android.os.Handler;
import android.util.Log;

import com.bitmovin.analytics.BitmovinAnalytics;
import com.bitmovin.analytics.BitmovinAnalyticsConfig;
import com.bitmovin.analytics.data.ErrorCode;
import com.bitmovin.analytics.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class PlayerStateMachine {
    private static final String TAG = "PlayerStateMachine";
    private final BitmovinAnalyticsConfig config;
    private List<StateMachineListener> listeners = new ArrayList<StateMachineListener>();
    private PlayerState currentState;
    private long elaspedTimeInitial = 0;
    private long elapsedTimeFirstReady = 0;
    private long elapsedTimeOnEnter = 0;
    private long elapsedTimeSeekStart = 0;
    private long videoTimeStart;
    private long videoTimeEnd;
    private ErrorCode errorCode;
    private String impressionId;
    private Handler heartbeatHandler = new Handler();
    private int heartbeatDelay = 59700; // default to 60 seconds
    private final BitmovinAnalytics analytics;

    public PlayerStateMachine(BitmovinAnalyticsConfig config, BitmovinAnalytics analytics) {
        this.config = config;
        this.analytics = analytics;
        this.heartbeatDelay = this.config.getHeartbeatInterval();
        resetStateMachine();
    }

    public void enableHeartbeat() {
        heartbeatHandler.postDelayed(new Runnable() {
            public void run() {
                long elapsedTime = Util.getElapsedTime();
                long elapsedTimeOnEnter = getElapsedTimeOnEnter();
                videoTimeEnd = analytics.getPosition();
                for (StateMachineListener listener : getListeners()) {
                    listener.onHeartbeat(elapsedTime - elapsedTimeOnEnter);
                }
                PlayerStateMachine.this.elapsedTimeOnEnter = elapsedTime;
                videoTimeStart = videoTimeEnd;
                heartbeatHandler.postDelayed(this, heartbeatDelay);
            }
        }, heartbeatDelay);
    }

    public void disableHeartbeat() {
        heartbeatHandler.removeCallbacksAndMessages(null);
    }

    public void resetStateMachine() {
        disableHeartbeat();
        this.impressionId = Util.getUUID();
        this.elaspedTimeInitial = Util.getElapsedTime();
        this.elapsedTimeFirstReady = 0;
        setCurrentState(PlayerState.SETUP);
    }

    public synchronized void transitionState(PlayerState destinationPlayerState, long videoTime) {
        long elapsedTime = Util.getElapsedTime();
        videoTimeEnd = videoTime;

        Log.d(TAG, "Transitioning from " + currentState.toString() + " to " + destinationPlayerState.toString());

        currentState.onExitState(this, elapsedTime, destinationPlayerState);
        this.elapsedTimeOnEnter = elapsedTime;
        videoTimeStart = videoTimeEnd;
        destinationPlayerState.onEnterState(this);
        setCurrentState(destinationPlayerState);
    }

    public long getElapsedTimeFirstReady() {
        return elapsedTimeFirstReady;
    }

    public void setElapsedTimeFirstReady(long elapsedTime) {
        this.elapsedTimeFirstReady = elapsedTime;
    }

    public void addListener(StateMachineListener toAdd) {
        listeners.add(toAdd);
    }

    public void removeListener(StateMachineListener toRemove) {
        listeners.remove(toRemove);
    }

    List<StateMachineListener> getListeners() {
        return listeners;
    }

    public PlayerState getCurrentState() {
        return currentState;
    }

    private void setCurrentState(final PlayerState newPlayerState) {
        this.currentState = newPlayerState;
    }

    public long getStartupTime() {
        return elapsedTimeFirstReady - elaspedTimeInitial;
    }

    public String getImpressionId() {
        return impressionId;
    }

    public long getVideoTimeStart() {
        return videoTimeStart;
    }

    public long getVideoTimeEnd() {
        return videoTimeEnd;
    }

    public long getElapsedTimeOnEnter() {
        return elapsedTimeOnEnter;
    }

    public long getElapsedTimeSeekStart() {
        return elapsedTimeSeekStart;
    }

    public void setElapsedTimeSeekStart(long elapsedTimeSeekStart) {
        this.elapsedTimeSeekStart = elapsedTimeSeekStart;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}


