package com.brentvatne.exoplayer

import android.media.MediaCodecList
import android.os.Build
import android.util.Log
import androidx.media3.common.MimeTypes

/**
 * Date: 10 Jan 2025
 * Author: Rohan Kumar Singh
 *
 * A utility class to check if the device supports Dolby audio formats.
 * This class uses the MediaCodec API to query available codecs on the device
 * and checks if any codec supports Dolby audio MIME types such as AC-3 or E-AC-3.
 *
 * **Key Features**:
 * 1. Ensures compatibility with devices running Android Lollipop (API 21) or higher.
 * 2. Skips encoder codecs and only checks decoders for Dolby audio support.
 * 3. Logs the results for debugging and provides an easy-to-use method to determine
 * if Dolby audio formats are supported.
 */
class AudioSupportChecker {
    private var TAG = "AudioSupportChecker";

    val isDolbyAudioSupported: Boolean
        get() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                // MediaCodecList API requires Lollipop or higher
                Log.w(TAG, "isDolbyAudioSupported: MediaCodecList not supported on this device.")
                return false
            }

            try {
                val codecList = MediaCodecList(MediaCodecList.ALL_CODECS)
                val codecInfos = codecList.codecInfos

                for (codecInfo in codecInfos) {
                    if (codecInfo.isEncoder) {
                        continue  // Skip encoders
                    }

                    val supportedTypes = codecInfo.supportedTypes
                    for (mimeType in supportedTypes) {
                        if (isDolbyMimeType(mimeType)) {
                            Log.d(TAG, "isDolbyAudioSupported: Dolby audio MIME type found: $mimeType")
                            return true
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "isDolbyAudioSupported: Error checking Dolby support", e)
            }

            Log.d(TAG, "isDolbyAudioSupported: No Dolby audio support found.")
            return false
        }

    private fun isDolbyMimeType(mimeType: String): Boolean {
        // Dolby audio MIME types
        return mimeType.equals(MimeTypes.AUDIO_E_AC3, ignoreCase = true) || mimeType.equals(MimeTypes.AUDIO_AC3, ignoreCase = true)
    }
}
