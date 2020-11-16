package com.robosolutions.temixtopsmarket.preference

import android.content.Context
import androidx.datastore.createDataStore
import com.robosolutions.temixtopsmarket.Preference
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