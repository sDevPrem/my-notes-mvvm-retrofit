package com.sdevprem.mynotes.data.repository

import com.sdevprem.mynotes.data.api.UserAPI
import com.sdevprem.mynotes.data.model.user.User
import com.sdevprem.mynotes.data.model.user.UserResponse
import com.sdevprem.mynotes.data.utils.TokenManager
import com.sdevprem.mynotes.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.Response
import javax.inject.Inject

private const val errorMsg = "Something went wrong"

class UserRepository @Inject constructor(
    private val userAPI: UserAPI,
    private val tokenManager: TokenManager
) {

    val authToken = tokenManager.authToken

    fun registerUser(user: User) = doAuth { userAPI.signup(user) }

    fun loginUser(user: User) = doAuth { userAPI.login(user) }

    private inline fun doAuth(
        crossinline authenticate: suspend () -> Response<UserResponse>,
    ) = flow<NetworkResult<UserResponse>> {
        try {
            val response = authenticate()
            if (response.isSuccessful && response.body() != null) {
                val userResponse = response.body()!!
                tokenManager.updateAuthToken(userResponse.token)
                emit(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                emit(NetworkResult.Error(response.errorBody()!!.charStream().readText()))
            } else {
                emit(NetworkResult.Error(errorMsg))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: errorMsg))
        }
    }.flowOn(Dispatchers.IO)
        .onStart { emit(NetworkResult.Loading) }
}