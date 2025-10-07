package com.example.nebeng.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintangjaya.cashierapp.data.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlin.onFailure
import kotlin.onSuccess
import kotlin.text.isEmpty

class AuthViewModel (
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        val user = repository.getCurrentUser()
        if (user == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            viewModelScope.launch {
                val roleResult = repository.getUserRole(user.uid)
                roleResult.onSuccess { role ->
                    _authState.value = AuthState.Authenticated(role)
                }.onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Failed to fetch role")
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty()) {
                _authState.value = AuthState.Error("Email or password can't be empty")
                return@launch
            }

            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
            result.onSuccess { user ->
                val roleResult = repository.getUserRole(user.uid)
                roleResult.onSuccess { role ->
                    _authState.value = AuthState.Authenticated(role)
                }.onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Failed to fetch role")
                }
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Login failed")
            }

        }
    }

    fun signup(
        email: String,
        password: String,
        name: String,
        nim: String,
        phone: String,
        photoUrl: String,
    ) {
        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty()) {
                _authState.value = AuthState.Error("Email or password can't be empty")
                return@launch
            }

            _authState.value = AuthState.Loading

            val result = repository.signUp(
                email,
                password,
                name,
                nim,
                phone,
                photoUrl
            )
            
            result.onSuccess { user ->
                val roleResult = repository.getUserRole(user.uid)
                roleResult.onSuccess { role ->
                    _authState.value = AuthState.Authenticated(role)
                }.onFailure {
                    _authState.value = AuthState.Error("Failed to fetch role")
                }
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Register failed")
            }
        }
    }

    fun signout() {
        repository.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    suspend fun getUserRole(uid: String): String? {
        return repository.getUserRole(uid).getOrNull()
    }
}

sealed class AuthState {
    data class Authenticated(val role: String) : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}