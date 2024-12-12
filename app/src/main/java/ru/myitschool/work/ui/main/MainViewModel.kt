package ru.myitschool.work.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.myitschool.work.data.remote.LoginApi
import ru.myitschool.work.data.remote.ErrorDto
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  @ApplicationContext private val context: Context,
  private val api: LoginApi
) : ViewModel() {
  private val _state = MutableStateFlow(MainState())
  val state = _state.asStateFlow()

  private val dfo = SimpleDateFormat("yyyy-MM-dd HH:mm")
  private val dfi= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")

  fun loadPersonInfo(username: String) {
    viewModelScope.launch {
      try {
        val info = api.info(username)
        _state.update {
          MainState(
            fullName = info.name,
            photo = info.photo,
            position = info.position,
            lastVisit = dfo.format(dfi.parse(info.lastVisit)!!),
            error = null
          )
        }
      } catch (e: HttpException) {
        e.response()?.errorBody()?.string()?.let { errorString ->
          val gson = GsonBuilder().create()
          val errorDto = gson.fromJson(errorString, ErrorDto::class.java)
          _state.update {
            MainState(
              error = errorDto.error
            )
          }
        }
      } catch (e: Exception) {
        _state.update { MainState(error = "Unknown error: ${e.message}") }
      }
    }
  }
}