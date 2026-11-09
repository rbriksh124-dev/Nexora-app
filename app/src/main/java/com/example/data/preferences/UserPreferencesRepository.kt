package com.example.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "nexora_prefs")

enum class AppThemeMode {
    DARK, LIGHT, SYSTEM
}

enum class AppLanguage {
    ENGLISH, HINDI
}

class UserPreferencesRepository(private val context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val KEY_THEME = stringPreferencesKey("app_theme")
        val KEY_LANG = stringPreferencesKey("app_language")
        val KEY_HAPTIC = booleanPreferencesKey("app_haptic")
    }

    val themeMode: Flow<AppThemeMode> = dataStore.data.map { prefs ->
        when (prefs[KEY_THEME]) {
            "LIGHT" -> AppThemeMode.LIGHT
            "SYSTEM" -> AppThemeMode.SYSTEM
            else -> AppThemeMode.DARK // Default futuristic dark
        }
    }

    val language: Flow<AppLanguage> = dataStore.data.map { prefs ->
        when (prefs[KEY_LANG]) {
            "HINDI" -> AppLanguage.HINDI
            else -> AppLanguage.ENGLISH
        }
    }

    val hapticEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_HAPTIC] ?: true
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        dataStore.edit { it[KEY_THEME] = mode.name }
    }

    suspend fun setLanguage(lang: AppLanguage) {
        dataStore.edit { it[KEY_LANG] = lang.name }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_HAPTIC] = enabled }
    }
}
