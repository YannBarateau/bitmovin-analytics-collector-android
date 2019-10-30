package com.bitmovin.analytics.bitmovin.player

import com.bitmovin.analytics.BitmovinAdAnalytics
import com.bitmovin.analytics.adapters.AdAdapter
import com.bitmovin.analytics.bitmovin.player.utils.AdBreakMapper
import com.bitmovin.analytics.bitmovin.player.utils.AdMapper
import com.bitmovin.analytics.bitmovin.player.utils.AdQuartileFactory
import com.bitmovin.analytics.data.AdModuleInformation
import com.bitmovin.player.BitmovinPlayer
import com.bitmovin.player.api.event.listener.*

/**
 * An adapter that maps the Ad Events to the BitmovinAdAnalytics class
 */
class BitmovinSdkAdAdapter(val bitmovinPlayer: BitmovinPlayer, val adAnalytics: BitmovinAdAnalytics) : AdAdapter {

    private val adMapper: AdMapper = AdMapper()
    private val adBreakMapper: AdBreakMapper = AdBreakMapper()
    private val adQuartileFactory: AdQuartileFactory = AdQuartileFactory()


    private val onAdStartedListener = OnAdStartedListener {
        if(it.ad == null){
            return@OnAdStartedListener
        }

        adAnalytics.onAdStarted(adMapper.FromPlayerAd(it.ad!!))
    }

    private val onAdFinishedListener = OnAdFinishedListener {
        adAnalytics.onAdFinished()
    }

    private val onAdBreakStartedListener = OnAdBreakStartedListener {
        if (it.adBreak == null){
            return@OnAdBreakStartedListener
        }

        adAnalytics.onAdBreakStarted(adBreakMapper.FromPlayerAdBreak(it.adBreak!!))
    }

    private val onAdBreakFinishedListener = OnAdBreakFinishedListener {
        adAnalytics.onAdBreakFinished()
    }

    private val onAdClickedListener = OnAdClickedListener {
        adAnalytics.onAdClicked(it.clickThroughUrl)
    }

    private val onAdErrorListener = OnAdErrorListener {
        if(it.adConfiguration == null)
        {
            return@OnAdErrorListener
        }

        adAnalytics.onAdError(
                adBreakMapper.FromPlayerAdConfiguration(it.adConfiguration!!),
                it.code,
                it.message)
    }

    private val onAdSkippedListener = OnAdSkippedListener {
        adAnalytics.onAdSkipped()
    }

    private val onAdManifestLoadedListener = OnAdManifestLoadedListener {
        if (it.adBreak == null){
            return@OnAdManifestLoadedListener
        }

        adAnalytics.onAdManifestLoaded(adBreakMapper.FromPlayerAdBreak(it.adBreak!!), it.downloadTime)
    }

    private val onPlayListener = OnPlayListener {
        adAnalytics.onPlay()
    }

    private val onPausedListener = OnPausedListener {
        adAnalytics.onPause()
    }

    private val onAdQuartileListener = OnAdQuartileListener {
        adAnalytics.onAdQuartile(adQuartileFactory.FromPlayerAdQuartile(it.quartile))
    }

    init {
        bitmovinPlayer.addEventListener(onAdStartedListener)
        bitmovinPlayer.addEventListener(onAdFinishedListener)
        bitmovinPlayer.addEventListener(onAdBreakStartedListener)
        bitmovinPlayer.addEventListener(onAdBreakFinishedListener)
        bitmovinPlayer.addEventListener(onAdClickedListener)
        bitmovinPlayer.addEventListener(onAdErrorListener)
        bitmovinPlayer.addEventListener(onAdSkippedListener)
        bitmovinPlayer.addEventListener(onAdManifestLoadedListener)
        bitmovinPlayer.addEventListener(onPlayListener)
        bitmovinPlayer.addEventListener(onPausedListener)
        bitmovinPlayer.addEventListener(onAdQuartileListener)
    }

    override fun release() {
        bitmovinPlayer.removeEventListener(onAdStartedListener)
        bitmovinPlayer.removeEventListener(onAdFinishedListener)
        bitmovinPlayer.removeEventListener(onAdBreakStartedListener)
        bitmovinPlayer.removeEventListener(onAdBreakFinishedListener)
        bitmovinPlayer.removeEventListener(onAdClickedListener)
        bitmovinPlayer.removeEventListener(onAdErrorListener)
        bitmovinPlayer.removeEventListener(onAdSkippedListener)
        bitmovinPlayer.removeEventListener(onAdManifestLoadedListener)
        bitmovinPlayer.removeEventListener(onPlayListener)
        bitmovinPlayer.removeEventListener(onPausedListener)
        bitmovinPlayer.removeEventListener(onAdQuartileListener)
    }

    override val isLinearAdActive: Boolean
        get() = bitmovinPlayer.isAd
    override val moduleInformation: AdModuleInformation
        // TODO get actual module from player
        get() = AdModuleInformation("DefaultAdvertisingService", BitmovinUtil.getPlayerVersion())
}