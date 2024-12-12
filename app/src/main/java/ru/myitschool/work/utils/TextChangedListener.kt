package ru.myitschool.work.utils

import android.text.Editable
import android.text.TextWatcher
import ru.myitschool.work.databinding.FragmentLoginBinding

class TextChangedListener(private val binding: FragmentLoginBinding) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val a = binding.username.text.toString()
        println(checker(a))
        if (checker(a) == true) {
            binding.login.isEnabled = true
        }else{
            binding.login.isEnabled = false
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun checker(login: String): Boolean {
        if (login.isEmpty() || login.length < 3 || login.isNotEmpty() && login[0].isDigit() || !login.matches(Regex("^[a-zA-Z0-9]+$"))) {
            return false
        }
        return true
    }
}