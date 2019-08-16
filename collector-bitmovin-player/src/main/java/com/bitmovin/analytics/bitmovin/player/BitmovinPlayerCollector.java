package com.bitmovin.analytics.bitmovin.player;

import android.content.Context;

import com.bitmovin.analytics.BitmovinAnalytics;
import com.bitmovin.analytics.BitmovinAnalyticsConfig;
import com.bitmovin.player.BitmovinPlayer;

public class BitmovinPlayerCollector extends BitmovinAnalytics {

  /**
   * Bitmovin Analytics
   *
   * @param bitmovinAnalyticsConfig {@link BitmovinAnalyticsConfig}
   */
  public BitmovinPlayerCollector(
          BitmovinAnalyticsConfig bitmovinAnalyticsConfig, Context context) {
    super(bitmovinAnalyticsConfig, context);
  }

  public void attachPlayer(BitmovinPlayer player) {
    BitmovinSdkAdapter adapter = new BitmovinSdkAdapter(player, this.bitmovinAnalyticsConfig, this.context,
            this.playerStateMachine);

    this.attach(adapter);
  }
}
