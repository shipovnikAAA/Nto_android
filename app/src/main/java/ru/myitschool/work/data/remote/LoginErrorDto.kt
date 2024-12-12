package ru.myitschool.work.data.remote

data class LoginErrorDto(
  val timestamp: String,
  val status: Long,
  val error: String,
  val path: String,
)
