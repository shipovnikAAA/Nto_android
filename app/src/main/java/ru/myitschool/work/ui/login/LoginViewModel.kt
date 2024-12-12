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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.myitschool.work.data.remote.LoginApi
import ru.myitschool.work.data.remote.LoginErrorDto
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: LoginApi
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private fun isUsernameValid(username: String): Boolean =
        !(username.length < 3 || username[0].isDigit() || !username.matches(Regex("^[a-zA-Z0-9]+$")))

    fun tryLogin(username: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                api.auth(username)
                Log.d("LoginViewModel", "Login success for $username")
                onSuccess()
            } catch (e: HttpException) {
                Log.e("LoginViewModel", "Login failed for $username", e)
                e.response()?.errorBody()?.string()?.let { errorString ->
                    val gson = GsonBuilder().create()
                    val errorDto = gson.fromJson(errorString, LoginErrorDto::class.java)
                    _state.update { it.copy(error = errorDto.error) }
                }
            }
        }
    }

    fun onUsernameChanged(username: String) =
        _state.update { it.copy(isLoginEnabled = isUsernameValid(username), error = null) }
}