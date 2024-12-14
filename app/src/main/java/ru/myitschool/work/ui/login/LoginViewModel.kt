package ru.myitschool.work.ui.login

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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: LoginApi,
    private val dataStoreManager: AppModule.DataStoreManager
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _savedUsername = MutableStateFlow<String?>(null)
    val savedUsername = _savedUsername.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.lastUsername.distinctUntilChanged().collect { lastUsername ->
                if (lastUsername.isNotEmpty()) {
                    _savedUsername.update { lastUsername }
                }
            }
        }
    }

    private fun isUsernameValid(username: String): Boolean =
        !(username.length < 3 || username[0].isDigit() || !username.matches(Regex("^[a-zA-Z0-9]+$")))

    fun tryLogin(username: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                api.auth(username)
                Log.d("LoginViewModel", "Login success for $username")
                dataStoreManager.setLastUsername(username)
                onSuccess()
            } catch (e: HttpException) {
                Log.e("LoginViewModel", "Login failed for $username", e)
                e.response()?.errorBody()?.string()?.let { errorString ->
                    val gson = GsonBuilder().create()
                    val errorDto = gson.fromJson(errorString, ErrorDto::class.java)
                    _state.update { it.copy(error = errorDto.error) }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login failed for $username", e)
               _state.update { it.copy(error = "Unknown error: ${e.message}") }
            }
        }
    }

    fun onUsernameChanged(username: String) =
        _state.update { it.copy(isLoginEnabled = isUsernameValid(username), error = null) }
}