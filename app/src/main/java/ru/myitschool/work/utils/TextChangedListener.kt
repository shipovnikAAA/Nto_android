package ru.myitschool.work.utils

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import ru.myitschool.work.databinding.FragmentLoginBinding

class TextChangedListener(private val onChange: (String) -> Unit) :
  TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
        onChange(s?.toString().orEmpty())

    override fun afterTextChanged(s: Editable?) = Unit
}