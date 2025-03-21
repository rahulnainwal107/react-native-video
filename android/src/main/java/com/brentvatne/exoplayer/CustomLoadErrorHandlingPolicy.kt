package com.brentvatne.exoplayer

import androidx.media3.datasource.HttpDataSource.InvalidResponseCodeException
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy.FallbackOptions
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy.FallbackSelection
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy.LoadErrorInfo
import java.io.IOException
import kotlin.math.min

class CustomLoadErrorHandlingPolicy : LoadErrorHandlingPolicy {
    override fun getFallbackSelectionFor(fallbackOptions: FallbackOptions, loadErrorInfo: LoadErrorInfo): FallbackSelection? {
        return null
    }

    override fun getRetryDelayMsFor(loadErrorInfo: LoadErrorInfo): Long {
        val exception = loadErrorInfo.exception
        val errorCount = loadErrorInfo.errorCount

        // Retry for any exception that is not a subclass of ParserException, java.io.FileNotFoundException,
        // HttpDataSource.CleartextNotPermittedException or Loader.UnexpectedLoaderException
        if (exception is InvalidResponseCodeException || exception is IOException) {
            val delay = INITIAL_RETRY_DELAY_MS * (1 shl min((errorCount - 1).toDouble(), 5.0).toInt()) // Exponential backoff
            return min(delay.toDouble(), MAX_RETRY_DELAY_MS.toDouble()).toLong()
        }
        return -1 // Do not retry
    }

    override fun getMinimumLoadableRetryCount(dataType: Int): Int {
        return MAX_LICENSE_RETRIES
    }

    companion object {
        private const val MAX_LICENSE_RETRIES = 3
        private const val INITIAL_RETRY_DELAY_MS: Long = 1000
        private const val MAX_RETRY_DELAY_MS: Long = 5000
    }
}
