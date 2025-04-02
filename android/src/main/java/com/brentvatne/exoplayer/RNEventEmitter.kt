package com.brentvatne.exoplayer

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.modules.core.DeviceEventManagerModule

/*
* RNEventEmitter - Low Network Detection (ALC-3097)
* Date Patch: 22 Jan 2025
* Author: Vibhash Kumar
* */
class RNEventEmitter(private val mReactContext: ReactContext) : ReactContextBaseJavaModule() {
    override fun getName(): String {
        return "RNEventEmitter"
    }

    fun sendEvent(data: HashMap<String?, String?>) {
        val writableMap = Arguments.createMap()

        // Convert HashMap to WritableMap
        for ((key, value) in data) {
            writableMap.putString(key!!, value)
        }

        if (mReactContext.hasActiveReactInstance()) {
            mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("NetworkEventData", writableMap)
        }
    }
} /* End of RNEventEmitter - (ALC-3097)  */
