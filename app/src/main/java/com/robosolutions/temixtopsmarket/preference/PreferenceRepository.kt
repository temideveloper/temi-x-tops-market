package com.robosolutions.temixtopsmarket.preference

import android.content.Context
import androidx.datastore.createDataStore
import com.robosolutions.temixtopsmarket.*
import com.robosolutions.temixtopsmarket.extensions.getInt
import com.robosolutions.temixtopsmarket.extensions.nonZeroOr
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

    /** General preference. */
    val general = preference.map { it.general }
        .map {
            it.toBuilder().apply {
                if (detectionRange == 0.0f) detectionRange = 1f // Set default detection range
            }.build()
        }

    suspend fun saveDetectionRange(range: Float) = saveGeneral { setDetectionRange(range) }

    suspend fun saveAutoReturnLocation(location: String) =
        saveGeneral { setAutoReturnLocation(location) }

    private suspend fun saveGeneral(block: General.Builder.() -> General.Builder) {
        savePreference { setGeneral(block(general.toBuilder()).build()) }
    }

    /** Delays preference. */
    val delays = preference.map { it.delays }
        .map {
            it.toBuilder().apply {
                autoReturn = autoReturn nonZeroOr context.getInt(R.integer.default_auto_return_ms)

                checkInReturn =
                    checkInReturn nonZeroOr context.getInt(R.integer.default_check_in_return_ms)

                excuseMeInterval =
                    excuseMeInterval nonZeroOr context.getInt(R.integer.default_excuse_me_interval_ms)
            }.build()
        }

    suspend fun saveAutoReturnDelay(delayMs: Int) = saveDelays { setAutoReturn(delayMs) }

    suspend fun saveCheckInReturnDelay(delayMs: Int) = saveDelays { setCheckInReturn(delayMs) }

    suspend fun saveExcuseMeInterval(delayMs: Int) = saveDelays { setExcuseMeInterval(delayMs) }

    private suspend fun saveDelays(block: Delays.Builder.() -> Delays.Builder) {
        savePreference { setDelays(block(delays.toBuilder()).build()) }
    }

    val locations = preference.map { it.locations }

    suspend fun saveZoneOne(location: String) = saveLocations { setZoneOne(location) }

    suspend fun saveZoneTwo(location: String) = saveLocations { setZoneTwo(location) }

    suspend fun saveZoneThree(location: String) = saveLocations { setZoneThree(location) }

    suspend fun saveZoneFour(location: String) = saveLocations { setZoneFour(location) }

    suspend fun saveZoneFive(location: String) = saveLocations { setZoneFive(location) }

    suspend fun saveZoneSix(location: String) = saveLocations { setZoneSix(location) }

    suspend fun saveZoneSeven(location: String) = saveLocations { setZoneSeven(location) }

    suspend fun saveZoneEight(location: String) = saveLocations { setZoneEight(location) }

    suspend fun saveZoneNine(location: String) = saveLocations { setZoneNine(location) }

    suspend fun saveZoneTen(location: String) = saveLocations { setZoneTen(location) }

    suspend fun saveZoneEleven(location: String) = saveLocations { setZoneEleven(location) }

    suspend fun saveZoneTwelve(location: String) = saveLocations { setZoneTwelve(location) }

    suspend fun saveZoneThirteen(location: String) = saveLocations { setZoneThirteen(location) }

    suspend fun saveZoneFourteen(location: String) = saveLocations { setZoneFourteen(location) }

    suspend fun saveZoneFifteen(location: String) = saveLocations { setZoneFifteen(location) }

    suspend fun saveZoneSixteen(location: String) = saveLocations { setZoneSixteen(location) }

    suspend fun saveZoneSeventeen(location: String) = saveLocations { setZoneSeventeen(location) }

    private suspend fun saveLocations(block: Locations.Builder.() -> Locations.Builder) {
        savePreference { setLocations(block(locations.toBuilder()).build()) }
    }

    /** Contains URL for QR Codes. */
    val qrCodeUrls = preference.map { it.qrCodeUrls }

    suspend fun saveThaiChanaUrl(url: String) = saveQrCode { setThaiChana(url) }

    suspend fun savePromotionUrl(url: String) = saveQrCode { setPromotion(url) }

    private suspend fun saveQrCode(block: QrCodeUrls.Builder.() -> QrCodeUrls.Builder) {
        savePreference { setQrCodeUrls(block(qrCodeUrls.toBuilder()).build()) }
    }

    /** Contains TTS messages. */
    val speech = preference.map { it.speech }
        .map {
            it.toBuilder().apply {
                if (greeting.isBlank()) {
                    greeting = context.getString(R.string.tts_complete_greeting)
                }

                if (recurringGreeting.isBlank()) {
                    recurringGreeting = context.getString(R.string.tts_partial_greeting)
                }
            }.build()
        }

    suspend fun saveGreeting(message: String) = saveSpeech { setGreeting(message) }

    suspend fun saveRecurringGreeting(message: String) =
        saveSpeech { setRecurringGreeting(message) }

    private suspend fun saveSpeech(block: Speech.Builder.() -> Speech.Builder) {
        savePreference { setSpeech(block(speech.toBuilder()).build()) }
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