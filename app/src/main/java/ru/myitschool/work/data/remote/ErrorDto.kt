package ru.myitschool.work.data.remote

data class ErrorDto(
  val timestamp: String,
  val status: Long,
  val error: String,
  val path: String,
)
