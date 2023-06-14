package com.sdevprem.mynotes.data.api

import com.sdevprem.mynotes.data.model.user.User
import com.sdevprem.mynotes.data.model.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("/signup")
    suspend fun signup(@Body user: User): Response<UserResponse>

    @POST("/login")
    suspend fun login(@Body user: User): Response<UserResponse>

}