package com.sdevprem.mynotes.data.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferenceKeys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    val authToken = dataStore
        .data
        .map {
            it[PreferenceKeys.AUTH_TOKEN]
        }

    suspend fun updateAuthToken(token: String) = dataStore
        .edit {
            it[PreferenceKeys.AUTH_TOKEN] = token
        }

}