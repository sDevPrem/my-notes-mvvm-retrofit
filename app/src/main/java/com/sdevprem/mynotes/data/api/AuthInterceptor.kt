package com.sdevprem.mynotes.data.api

import com.sdevprem.mynotes.data.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking(Dispatchers.IO) {
        val request = chain
            .request()
            .newBuilder()

        val token = tokenManager.authToken.first()
        request.addHeader("Authorization", "Bearer $token")

        return@runBlocking chain.proceed(request.build())
    }
}