package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.FirebaseAuthenticationManager
import com.example.data.catalog.ToolCatalog
import com.example.data.models.FavoriteItem
import com.example.data.models.HistoryItem
import com.example.data.models.ToolCategory
import com.example.data.models.UtilityTool
import com.example.data.preferences.AppLanguage
import com.example.data.preferences.AppThemeMode
import com.example.data.preferences.UserPreferencesRepository
import com.example.data.repository.NexoraRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val repository = NexoraRepository.create(application)
    val preferences = UserPreferencesRepository(application)
    val authManager = FirebaseAuthenticationManager(application)

    val currentUser: StateFlow<FirebaseUser?> = authManager.currentUserState

    init {
        authManager.attemptAutoSignIn(viewModelScope)
    }

    val themeMode: StateFlow<AppThemeMode> = preferences.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppThemeMode.DARK)

    val language: StateFlow<AppLanguage> = preferences.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppLanguage.ENGLISH)

    val hapticEnabled: StateFlow<Boolean> = preferences.hapticEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val historyList: StateFlow<List<HistoryItem>> = repository.historyList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoritesList: StateFlow<List<FavoriteItem>> = repository.favoritesList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<UtilityTool>>(emptyList())
    val searchResults: StateFlow<List<UtilityTool>> = _searchResults.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _searchResults.value = ToolCatalog.searchTools(query)
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun toggleFavorite(tool: UtilityTool, isFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(tool.id, tool.nameEn, tool.category.id, isFav)
        }
    }

    fun logHistory(tool: UtilityTool, input: String, result: String) {
        viewModelScope.launch {
            repository.recordHistory(tool.id, tool.nameEn, tool.category.id, input, result)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun setTheme(mode: AppThemeMode) {
        viewModelScope.launch {
            preferences.setThemeMode(mode)
        }
    }

    fun setLanguage(lang: AppLanguage) {
        viewModelScope.launch {
            preferences.setLanguage(lang)
        }
    }

    fun setHaptic(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setHapticEnabled(enabled)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllLocalData()
        }
    }
}
