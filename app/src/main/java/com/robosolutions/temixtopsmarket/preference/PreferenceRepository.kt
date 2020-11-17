package com.robosolutions.temixtopsmarket.preference

import android.content.Context
import androidx.datastore.createDataStore
import com.robosolutions.temixtopsmarket.Delays
import com.robosolutions.temixtopsmarket.Preference
import com.robosolutions.temixtopsmarket.QrCodeUrls
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.extensions.getInt
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class PreferenceRepository @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.createDataStore(DATA_STORE_NAME, PreferenceSerializer)

    /** Contains this application's preferences. */
    private val preference = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading preference.")
                emit(Preference.getDefaultInstance())
            } else {
                throw exception
            }
        }

    /** Password for accessing admin panel. */
    val password = preference.map { it.password }

    /**
     * Persists admin panel password.
     *
     * @param encrypted The encrypted password.
     * @param iv The initialization used for encryption.
     */
    suspend fun savePassword(encrypted: String, iv: String) {
        savePreference {
            val newCredentials = password.toBuilder()
                .setPassword(encrypted)
                .setIv(iv)

            setPassword(newCredentials)
        }
    }

    /** Delays preference. */
    val delays = preference.map { it.delays }
        .map {
            it.toBuilder().apply {
                autoReturn =
                    autoReturn.coerceAtLeast(context.getInt(R.integer.default_auto_return_ms))

                checkInReturn =
                    checkInReturn.coerceAtLeast(context.getInt(R.integer.default_check_in_return_ms))

                excuseMeInterval =
                    excuseMeInterval.coerceAtLeast(context.getInt(R.integer.default_excuse_me_interval_ms))
            }.build()
        }

    suspend fun saveAutoReturnDelay(delayMs: Int) = saveDelays { setAutoReturn(delayMs) }

    suspend fun saveCheckInReturnDelay(delayMs: Int) = saveDelays { setCheckInReturn(delayMs) }

    suspend fun saveExcuseMeInterval(delayMs: Int) = saveDelays { setExcuseMeInterval(delayMs) }

    private suspend fun saveDelays(block: Delays.Builder.() -> Delays.Builder) {
        savePreference { setDelays(block(delays.toBuilder()).build()) }
    }

    /** Contains URL for QR Codes. */
    val qrCodeUrls = preference.map { it.qrCodeUrls }

    suspend fun saveThaiChanaUrl(url: String) = saveQrCode { setThaiChana(url) }

    suspend fun savePromotionUrl(url: String) = saveQrCode { setPromotion(url) }

    private suspend fun saveQrCode(block: QrCodeUrls.Builder.() -> QrCodeUrls.Builder) {
        savePreference { setQrCodeUrls(block(qrCodeUrls.toBuilder()).build()) }
    }

    /**
     * Saves data into the application's settings.
     *
     * @param block Data changes to be made.
     */
    private suspend fun savePreference(block: Preference.Builder.() -> Preference.Builder) {
        dataStore.updateData {
            it.toBuilder().apply { block(this) }.build()
        }
    }

    companion object {
        const val DATA_STORE_NAME = "preferences"
    }
}