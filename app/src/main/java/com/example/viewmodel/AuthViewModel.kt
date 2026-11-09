package com.example.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.FirebaseAuthenticationManager
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: FirebaseUser) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    val authManager = FirebaseAuthenticationManager(application)

    val currentUser: StateFlow<FirebaseUser?> = authManager.currentUserState

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        authManager.attemptAutoSignIn(viewModelScope) { success ->
            if (success) {
                authManager.currentUser?.let {
                    _uiState.value = AuthUiState.Success(it)
                }
            }
        }
    }

    fun signInWithGoogle(activity: Activity) {
        _uiState.value = AuthUiState.Loading
        authManager.signInWithGoogle(
            activity = activity,
            scope = viewModelScope,
            onSuccess = { user ->
                _uiState.value = AuthUiState.Success(user)
            },
            onError = { error ->
                _uiState.value = AuthUiState.Error(error)
            }
        )
    }

    fun signOut(onComplete: () -> Unit = {}) {
        authManager.signOut(viewModelScope) {
            _uiState.value = AuthUiState.Idle
            onComplete()
        }
    }

    fun clearError() {
        if (_uiState.value is AuthUiState.Error) {
            _uiState.value = AuthUiState.Idle
        }
        authManager.clearError()
    }
}
