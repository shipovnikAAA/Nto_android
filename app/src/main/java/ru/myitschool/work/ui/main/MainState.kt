package ru.myitschool.work.ui.main

data class MainState(
  val fullName: String = "",
  val photo: String = "",
  val position: String = "",
  val lastVisit: String = "",
  val error: String? = null
)
