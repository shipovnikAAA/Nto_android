package ru.myitschool.work.ui.qr.result

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.myitschool.work.data.remote.LoginApi
import ru.myitschool.work.data.remote.OpenWithCodeRequest
import javax.inject.Inject

@HiltViewModel
class QrResultViewModel @Inject constructor(
  @ApplicationContext private val context: Context,
  private val api: LoginApi
): ViewModel() {
  enum class QrResultSource { SUCCESS, FAILURE, CANCEL }

  private val _result = MutableStateFlow(QrResultSource.CANCEL)
  val result = _result.asStateFlow()

  fun tryParseData(username: String, data: String?) {
    viewModelScope.launch {
      if (data == null) {
        _result.update {
          QrResultSource.CANCEL
        }
      } else {
        try {
          api.open(username, OpenWithCodeRequest(data.toLong()))
          _result.update { QrResultSource.SUCCESS }
        } catch (e: HttpException) {
          e.response()?.errorBody()?.string()?.let { errorString ->
            _result.update { QrResultSource.FAILURE }
          }
        }
      }
    }
  }
}