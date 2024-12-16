package ru.myitschool.work.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.myitschool.work.data.remote.LoginApi
import ru.myitschool.work.data.remote.ErrorDto
import ru.myitschool.work.di.AppModule
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  @ApplicationContext private val context: Context,
  private val api: LoginApi,
  private val dataStoreManager: AppModule.DataStoreManager
) : ViewModel() {
  private val _state = MutableStateFlow(MainState())
  val state = _state.asStateFlow()

  private val dfo = SimpleDateFormat("yyyy-MM-dd HH:mm")
  private val dfi= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

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
      } catch (httpException: HttpException) {
        try {
          httpException.response()?.errorBody()?.string()?.let { errorString ->
            val gson = GsonBuilder().create()
            val errorDto = gson.fromJson(errorString, ErrorDto::class.java)
            _state.update {
              MainState(
                error = errorDto.error
              )
            }
          }
        } catch (e: Exception) {
          _state.update { MainState(error = httpException.message()) }
        }
      } catch (e: Exception) {
        _state.update { MainState(error = "Unknown error: ${e.message}") }
      }
    }
  }

  fun logout(onLogout: () -> Unit) {
    viewModelScope.launch {
      dataStoreManager.setLastUsername("")
      onLogout()
    }
  }
}