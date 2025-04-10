/**
 * * https://pulse.conviva.com/learning-center/content/sensor_developer_center/sensor_integration/android/android_quick_integration.htm
 *
 */
package com.brentvatne.react

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.exoplayer.ima.ImaAdsLoader
import com.brentvatne.react.BuildConfig
import com.conviva.sdk.ConvivaAdAnalytics
import com.conviva.sdk.ConvivaAnalytics
import com.conviva.sdk.ConvivaSdkConstants
import com.conviva.sdk.ConvivaSdkConstants.AdType
import com.conviva.sdk.ConvivaVideoAnalytics
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType

class ConvivaHelper(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "ConvivaHelper"
    }

    /**
     * Initialize the top-level object.
     */
    @ReactMethod
    fun initConviva(customerKey: String?, gatewayUrl: String?, enableTouchstone: Boolean ) {
        if (enableTouchstone || BuildConfig.DEBUG) {
            val settings: MutableMap<String, Any> = HashMap()
            if (gatewayUrl != null) {
                settings[ConvivaSdkConstants.GATEWAY_URL] = gatewayUrl
            }
            settings[ConvivaSdkConstants.LOG_LEVEL] = ConvivaSdkConstants.LogLevel.DEBUG
            ConvivaAnalytics.init(reactContext, customerKey, settings, null)
        } else {
            //production release
            ConvivaAnalytics.init(reactContext, customerKey)
        }
    }

    /**
     * This object needs to be instantiated once for each video playback, and is used to report the video-related events
     * in an application. Typically, it is instantiated after the init() call, and ideally just before the user hits play.
     */
    @ReactMethod
    fun buildVideoAnalytics() {
        videoAnalytics = ConvivaAnalytics.buildVideoAnalytics(this.reactContext)
    }

    @ReactMethod
    fun reportAdLoaded(adInfo: ReadableMap?) {
        if (videoAdAnalytics != null && adInfo != null) {
            val hashMap = readableMapToHashMap(adInfo)
            videoAdAnalytics!!.reportAdLoaded(hashMap)
        } else {
            Log.e("playerInfo conviva", "setPlayerInfo: playerInfo is null")
        }
    }

    @ReactMethod
    fun reportAdStarted(adInfo: ReadableMap?) {
        if (videoAdAnalytics != null && adInfo != null) {
            val hashMap = readableMapToHashMap(adInfo)
            videoAdAnalytics!!.reportAdStarted(hashMap)
        } else {
            Log.e("playerInfo conviva", "setPlayerInfo: playerInfo is null")
        }
    }

    fun _reportAdStarted() {
        if (videoAdAnalytics != null) {
            videoAdAnalytics!!.reportAdStarted()
        } else {
            Log.e("playerInfo conviva", "setPlayerInfo: playerInfo is null")
        }
    }

    @ReactMethod
    fun reportAdEnded() {
        if (videoAdAnalytics != null) {
            videoAdAnalytics!!.reportAdEnded()
        } else {
            Log.e("playerInfo conviva", "setPlayerInfo: playerInfo is null")
        }
    }

    /**
     * Call this API to set the Predefined metadata.
     */
    fun setPlayerInfo(playerInfo: ReadableMap?) {
        if (videoAnalytics != null && playerInfo != null) {
            val hashMap = readableMapToHashMap(playerInfo)
            videoAnalytics!!.setPlayerInfo(hashMap)
        } else {
            Log.e("playerInfo conviva", "setPlayerInfo: playerInfo is null")
        }
    }

    /**
     * Call this API to update or content info.
     * Conviva.assetName, c3.cm.contentType, my_custom_tag_key
     */
    @ReactMethod
    fun _reportAdBreakStarted(isCSAIad: Boolean, isUsingExternalPlayer: Boolean) {
        if (videoAnalytics != null && videoAdAnalytics != null) {
            if (isUsingExternalPlayer && isCSAIad) {
                videoAnalytics!!.reportAdBreakStarted(ConvivaSdkConstants.AdPlayer.CONTENT, AdType.CLIENT_SIDE)
            } else if (isUsingExternalPlayer && !isCSAIad) {
                videoAnalytics!!.reportAdBreakStarted(ConvivaSdkConstants.AdPlayer.CONTENT, AdType.SERVER_SIDE)
            } else if (!isUsingExternalPlayer && isCSAIad) {
                videoAnalytics!!.reportAdBreakStarted(ConvivaSdkConstants.AdPlayer.CONTENT, AdType.CLIENT_SIDE)
            } else {
                videoAnalytics!!.reportAdBreakStarted(ConvivaSdkConstants.AdPlayer.CONTENT, AdType.SERVER_SIDE)
            }
        } else {
            Log.e("contentInfo conviva", "setContentInfo: contentInfo is null")
        }
    }

    @ReactMethod
    fun reportAdError(errorMessage: String?) {
        if (videoAdAnalytics != null && errorMessage != null) {
            videoAdAnalytics!!.reportAdError(errorMessage, ConvivaSdkConstants.ErrorSeverity.FATAL)
        }
    }

    @ReactMethod
    fun reportAdSkipped() {
        if (videoAdAnalytics != null) {
            videoAdAnalytics!!.reportAdSkipped()
        }
    }

    @ReactMethod
    fun reportAdPlayPausedState(isPaused: Boolean) {
        if (videoAdAnalytics != null) {
            if (isPaused) {
                videoAdAnalytics!!.reportAdMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.PAUSED)
            } else {
                videoAdAnalytics!!.reportAdMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.PLAYING)
            }
        }
    }

    @ReactMethod
    fun reportAdBitrate(bitrate: Int?) {
        if (videoAdAnalytics != null) {
            videoAdAnalytics!!.reportAdMetric(ConvivaSdkConstants.PLAYBACK.BITRATE, bitrate)
        }
    }

    @ReactMethod
    fun setContentInfo(contentInfo: ReadableMap?) {
        if (videoAnalytics != null && contentInfo != null) {
            val hashMap = readableMapToHashMap(contentInfo)
            videoAnalytics!!.setContentInfo(hashMap)
        } else {
            Log.e("contentInfo conviva", "setContentInfo: contentInfo is null")
        }
    }

    fun reportAdBreakStarted(adPlayer: ConvivaSdkConstants.AdPlayer?, adType: AdType?) {
        if (videoAnalytics != null) {
            videoAnalytics!!.reportAdBreakStarted(adPlayer, adType)
        }
    }

    @ReactMethod
    fun _reportAdBreakEnded() {
        reportAdBreakEnded();
    }

    @ReactMethod
    fun reportPlaybackEndedFromRN() {
        if (videoAnalytics != null) {
            videoAnalytics!!.reportPlaybackEnded()
        }
    }

    fun reportPlaybackEventWaitStarted() {
        if (videoAnalytics != null) {
            videoAnalytics!!.reportPlaybackEvent(ConvivaSdkConstants.Events.USER_WAIT_STARTED.toString())
        }
    }

    fun reportPlaybackEventWaitEnded() {
        if (videoAnalytics != null) {
            videoAnalytics!!.reportPlaybackEvent(ConvivaSdkConstants.Events.USER_WAIT_ENDED.toString())
        }
    }

    @ReactMethod
    fun reportAppBackgrounded() {
        ConvivaAnalytics.reportAppBackgrounded()
    }

    @ReactMethod
    fun reportAppForegrounded() {
        ConvivaAnalytics.reportAppForegrounded()
    }

    // Report Errors
    fun reportPlaybackFailed(errorMessage: String?, contentInfo: Map<String?, Any?>?) {
        if (videoAnalytics != null) {
            videoAnalytics!!.reportPlaybackFailed(errorMessage, contentInfo)
        }
    }

    // Report Custom Playback Events
    fun reportPlaybackEvent(eventType: String?, eventDetail: Map<String?, Any?>?) {
        if (videoAnalytics != null) {
            videoAnalytics!!.reportPlaybackEvent(eventType, eventDetail)
        }
    }

    // Report Playback Error
    @ReactMethod
    fun reportPlaybackError(message: String?) {
        if (videoAnalytics != null) {
            videoAnalytics!!.reportPlaybackError(message, ConvivaSdkConstants.ErrorSeverity.FATAL)
        }
    }

    /**
     * Call these APIs to release the object on player's sudden exit, application's graceful exit, or when the Conviva object needs to be destroyed.
     */
    @ReactMethod
    fun releaseVideoAnalyticsFromRN() {
        if (videoAnalytics != null) {
            videoAnalytics!!.release()
            videoAnalytics = null
        }
    }

    @ReactMethod
    fun releaseConvivaAnalytics() {
        videoAnalytics = null
        ConvivaAnalytics.release()
    }

    @ReactMethod
    fun resetConvivaReloadingState() {
    }

    companion object {
        var videoAnalytics: ConvivaVideoAnalytics? = null
        var videoAdAnalytics: ConvivaAdAnalytics? = null
        private var reactContext: ReactApplicationContext? = null

        /**
         * Initialize context for use in static methods
         */
        fun initialize(context: ReactApplicationContext) {
            reactContext = context
        }

        @JvmStatic
        fun buildVideoAnalytics(_reactContext: Context?): ConvivaVideoAnalytics? {
            if (videoAnalytics == null) {
                videoAnalytics = ConvivaAnalytics.buildVideoAnalytics(_reactContext)
            }
            buildAdAnalytics(_reactContext)
            return videoAnalytics
        }

        fun buildAdAnalytics(_reactContext: Context?): ConvivaAdAnalytics {
            if (videoAdAnalytics == null) {
                videoAdAnalytics = ConvivaAnalytics.buildAdAnalytics(_reactContext, videoAnalytics)
            }
            return videoAdAnalytics!!
        }

        fun enableAdMetricAndMetadataDetection(adTagUrl: Uri?, isUsingExternalPlayer: Boolean, adsLoader: ImaAdsLoader?) {
            val info: MutableMap<String, Any> = HashMap()
            if (videoAdAnalytics != null && adTagUrl != null) {
                info[ConvivaSdkConstants.AD_TAG_URL] = adTagUrl
                if (isUsingExternalPlayer == true) {
                    info[ConvivaSdkConstants.AD_PLAYER] = ConvivaSdkConstants.AdPlayer.SEPARATE.toString()
                } else {
                    info[ConvivaSdkConstants.AD_PLAYER] = ConvivaSdkConstants.AdPlayer.CONTENT.toString()
                }
                videoAdAnalytics!!.setAdListener(adsLoader, info)
            }
        }

        @JvmStatic
        fun _reportAdEnded() {
            if (videoAdAnalytics != null) {
                videoAdAnalytics!!.reportAdEnded()
            } else {
                Log.e("playerInfo conviva", "setPlayerInfo: playerInfo is null")
            }
        }

        fun setContentInfo(contentInfo: Map<String?, Any?>?) {
            if (videoAnalytics != null && contentInfo != null) {
                videoAnalytics!!.setContentInfo(contentInfo)
            } else {
                Log.e("contentInfo conviva", "setContentInfo: contentInfo is null")
            }
        }

        // Reports Events and Metadata
        fun reportPlaybackRequested(_videoAnalytics: ConvivaVideoAnalytics, contentInfo: Map<String?, Any?>?) {
            if (contentInfo != null) {
                _videoAnalytics.reportPlaybackRequested(contentInfo)
            } else {
                Log.e("contentInfo conviva", "setContentInfo: contentInfo is null")
            }
        }

        @JvmStatic
        fun setPlayerReference(
            videoAnalytics: ConvivaVideoAnalytics?,
            newPlayer: Any?,
            convivaContentInfo: Map<String?, Any?>?,
            adTagUrl: Uri?,
            adsLoader: ImaAdsLoader?
        ) {
            if (videoAnalytics != null && newPlayer != null) {
                enableAdMetricAndMetadataDetection(adTagUrl, false, adsLoader)
                setContentInfo(convivaContentInfo)
                videoAnalytics.setPlayer(newPlayer)
                reportPlaybackRequested(videoAnalytics, convivaContentInfo)
            }
        }

        @JvmStatic
        fun reportPlaybackEnded() {
            if (videoAnalytics != null) {
                videoAnalytics!!.reportPlaybackEnded()
            }
        }

        @JvmStatic
        fun releaseVideoAnalytics() {
            if (videoAnalytics != null) {
                videoAnalytics!!.release()
                videoAnalytics = null
            }
        }

        @JvmStatic
        fun releaseVideoAdAnalytics() {
            if (videoAdAnalytics != null) {
                videoAdAnalytics!!.release()
                videoAdAnalytics = null
            }
        }

        @JvmStatic
        fun reportAdBreakEnded() {
            if (videoAnalytics != null) {
                videoAnalytics!!.reportAdBreakEnded()
            }
        }

        @JvmStatic
        fun reportAdMetric(key: String?, value: Any?) {
            if (videoAdAnalytics != null) {
                videoAdAnalytics!!.reportAdMetric(key, value)
            }
        }

        fun readableMapToHashMap(readableMap: ReadableMap?): HashMap<String, Any?> {
            val hashMap = HashMap<String, Any?>()
            if (readableMap != null) {
                val iterator = readableMap.keySetIterator()
                while (iterator.hasNextKey()) {
                    val key = iterator.nextKey()
                    when (readableMap.getType(key)) {
                        ReadableType.Null -> hashMap[key] = null
                        ReadableType.Boolean -> hashMap[key] = readableMap.getBoolean(key)
                        ReadableType.Number -> hashMap[key] = readableMap.getDouble(key)
                        ReadableType.String -> hashMap[key] = readableMap.getString(key)
                        ReadableType.Map -> hashMap[key] = readableMapToHashMap(readableMap.getMap(key))
                        ReadableType.Array -> {}
                    }
                }
            }
            return hashMap
        }
    }
}
