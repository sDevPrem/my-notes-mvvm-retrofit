package com.sdevprem.mynotes.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdevprem.mynotes.data.model.user.User
import com.sdevprem.mynotes.data.model.user.UserResponse
import com.sdevprem.mynotes.data.repository.UserRepository
import com.sdevprem.mynotes.utils.NetworkResult
import com.sdevprem.mynotes.utils.toReadOnly
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userResponse = MutableStateFlow<NetworkResult<UserResponse>>(NetworkResult.Idle)
    val userResponse = _userResponse.toReadOnly()

    val authToken = userRepository
        .authToken

    fun registerUser(user: User) {
        userRepository.registerUser(user)
            .onEach { _userResponse.value = it }
            .launchIn(viewModelScope)
    }

    fun loginUser(user: User) {
        userRepository.loginUser(user)
            .onEach { _userResponse.value = it }
            .launchIn(viewModelScope)
    }

    fun validateCredential(user: User, isLogin: Boolean): Pair<Boolean, String?> {
        var result: Pair<Boolean, String?> = true to null
        if ((!isLogin && user.userName.isBlank()) || user.email.isBlank() || user.password.isBlank()) {
            result = false to "Please provide valid credentials"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
            result = false to "Please provide valid email"
        } else if (user.password.length < 5) {
            result = false to "Password length should be greater than 5"
        }
        return result
    }
}