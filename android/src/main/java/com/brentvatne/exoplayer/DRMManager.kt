package com.brentvatne.exoplayer

import androidx.media3.common.util.Util
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import androidx.media3.exoplayer.drm.DrmSessionManager
import androidx.media3.exoplayer.drm.FrameworkMediaDrm
import androidx.media3.exoplayer.drm.HttpMediaDrmCallback
import androidx.media3.exoplayer.drm.UnsupportedDrmException
import com.brentvatne.common.api.DRMProps
import java.util.UUID

class DRMManager(private val dataSourceFactory: HttpDataSource.Factory) : DRMManagerSpec {
    private var hasDrmFailed = false

    @Throws(UnsupportedDrmException::class)
    override fun buildDrmSessionManager(uuid: UUID, drmProps: DRMProps): DrmSessionManager? = buildDrmSessionManager(uuid, drmProps, 0)

    @Throws(UnsupportedDrmException::class)
    private fun buildDrmSessionManager(uuid: UUID, drmProps: DRMProps, retryCount: Int = 0): DrmSessionManager? {
        if (Util.SDK_INT < 18) {
            return null
        }

        try {
            val drmCallback = HttpMediaDrmCallback(drmProps.drmLicenseServer, dataSourceFactory)

            // Set DRM headers
            val keyRequestPropertiesArray = drmProps.drmLicenseHeader
            for (i in keyRequestPropertiesArray.indices step 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i], keyRequestPropertiesArray[i + 1])
            }

            val mediaDrm = FrameworkMediaDrm.newInstance(uuid)


            /*
             * Author: Kaushal Gupta
             * Date: 27 Feb 2025
             * Issue:
            [1000]: ERROR_CODE_UNSPECIFIED - android.media.MediaCodec$CryptoException: Error decrypting data: requested key has not been loaded: ERROR_DRM_NO_LICENSE
             * */
            val securityLevel = mediaDrm.getPropertyString("securityLevel")
            if(securityLevel.equals("L1")){
                mediaDrm.setPropertyString("securityLevel", "L1");
            }
            else{
                mediaDrm.setPropertyString("securityLevel", "L3");
            }
            if (hasDrmFailed) {
                // When DRM fails using L1 we want to switch to L3
                mediaDrm.setPropertyString("securityLevel", "L3");
            }
            return DefaultDrmSessionManager.Builder()
                .setUuidAndExoMediaDrmProvider(uuid) { mediaDrm }
                .setKeyRequestParameters(null)
                .setMultiSession(drmProps.multiDrm)
                // .setLoadErrorHandlingPolicy(new CustomLoadErrorHandlingPolicy())
                .build(drmCallback)
        } catch (ex: UnsupportedDrmException) {
            hasDrmFailed = true
            throw ex
        } catch (ex: Exception) {
            if (retryCount < 3) {
                // Attempt retry 3 times in case where the OS Media DRM Framework fails for whatever reason
                hasDrmFailed = true
                return buildDrmSessionManager(uuid, drmProps, retryCount + 1)
            }
            throw UnsupportedDrmException(UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME, ex)
        }
    }
}
