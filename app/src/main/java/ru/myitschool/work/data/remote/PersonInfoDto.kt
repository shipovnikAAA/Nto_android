package ru.myitschool.work.data.remote

data class PersonInfoDto(
  val id: Long,
  val login: String,
  val name: String,
  val photo: String,
  val position: String,
  val lastVisit: String,
)
