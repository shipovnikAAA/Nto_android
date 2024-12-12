package ru.myitschool.work.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.databinding.FragmentLoginBinding
import ru.myitschool.work.databinding.FragmentMainBinding
import ru.myitschool.work.utils.TextChangedListener

@AndroidEntryPoint
class MainFragment: Fragment(R.layout.fragment_main) {
  private var _binding: FragmentMainBinding? = null
  private val binding: FragmentMainBinding get() = _binding!!

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    _binding = FragmentMainBinding.bind(view)


  }
}