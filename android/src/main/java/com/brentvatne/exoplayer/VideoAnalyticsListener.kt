package com.brentvatne.exoplayer

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.analytics.AnalyticsListener.EventTime
import androidx.media3.exoplayer.source.LoadEventInfo
import androidx.media3.exoplayer.source.MediaLoadData

/*
* VideoAnalyticsListener -Low Network Detection (ALC-3097)
* Date Patch: 22 Jan 2025
* Author: Vibhash Kumar
* */
interface VideoAnalyticsCallback {
    fun onAnalyticsDataReceived(data: HashMap<String, String>?)
}

class VideoAnalyticsListener(private val context: Context, private val videoAnalyticsCallback: VideoAnalyticsCallback) : AnalyticsListener {
    override fun onLoadCompleted(eventTime: EventTime, loadEventInfo: LoadEventInfo, mediaLoadData: MediaLoadData) {
        val bytesLoaded = loadEventInfo.bytesLoaded // Size of the chunk in bytes
        val downloadTimeMs = loadEventInfo.loadDurationMs // Time to download in milliseconds
        val speedKbps = (bytesLoaded * 8.0) / (downloadTimeMs) / 1024 // Speed in kbps
        val networkType = networkType
        val dict = HashMap<String, String>()
        dict["downloadTime"] = downloadTimeMs.toString()
        dict["size"] = bytesLoaded.toString()
        dict["reachability"] = networkType
        dict["downloadSpeedKBps"] = speedKbps.toString()
        videoAnalyticsCallback.onAnalyticsDataReceived(dict)
    }


    private val networkType: String
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        get() {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm != null) {
                val nc = cm.getNetworkCapabilities(cm.activeNetwork)
                if (nc != null) {
                    if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return "Wi-Fi"
                    } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return "Cellular"
                    } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return "Ethernet"
                    }
                }
            }
            return "Unknown"
        }
} /* End of Video Analytic Listener - (ALC-3097) */
