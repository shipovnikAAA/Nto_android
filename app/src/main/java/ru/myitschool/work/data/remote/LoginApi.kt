package ru.myitschool.work.data.remote

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface LoginApi {
  @GET("{login}/auth")
  suspend fun auth(@Path("login") login: String): ResponseBody

  @GET("{login}/info")
  suspend fun info(@Path("login") login: String): PersonInfoDto

  @PATCH("{login}/open")
  suspend fun open(@Path("login") login: String, @Body request: OpenWithCodeRequest): ResponseBody
}