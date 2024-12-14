package ru.myitschool.work.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.toRoute
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.databinding.FragmentMainBinding
import ru.myitschool.work.ui.login.LoginDestination
import ru.myitschool.work.utils.collectWhenStarted

@AndroidEntryPoint
class MainFragment: Fragment(R.layout.fragment_main) {
  private var _binding: FragmentMainBinding? = null
  private val binding: FragmentMainBinding get() = _binding!!
  private val viewModel: MainViewModel by viewModels()
  private val picasso: Picasso by lazy { Picasso.get() }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    _binding = FragmentMainBinding.bind(view)

    val username = findNavController().currentBackStackEntry?.toRoute<MainDestination>()?.username
    username?.let { user ->
      viewModel.loadPersonInfo(user)
      binding.refresh.setOnClickListener { viewModel.loadPersonInfo(user) }
      binding.logout.setOnClickListener {
        viewModel.logout()
        findNavController().navigateUp()
      }
    }
    subscribe()
  }

  private fun subscribe() {
    viewModel.state.collectWhenStarted(this) { state ->
      if (state.photo.isNotEmpty()) {
        picasso.load(state.photo).into(binding.photo)
      }
      binding.fullname.text = state.fullName
      binding.position.text = state.position
      binding.lastEntry.text = state.lastVisit
      setError(state.error)
    }
  }

  private fun setError(error: String?) {
    val showError = error != null
    val views = listOf(binding.fullname, binding.position, binding.lastEntry, binding.photo, binding.scan, binding.logout)
    views.forEach { it.visibility = if (showError) View.GONE else View.VISIBLE}
    binding.error.visibility = if (showError) View.VISIBLE else View.GONE
    binding.error.text = error ?: ""
  }
}