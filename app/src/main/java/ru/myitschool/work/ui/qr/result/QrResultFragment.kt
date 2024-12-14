package ru.myitschool.work.ui.qr.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.databinding.FragmentQrResultBinding
import ru.myitschool.work.ui.main.MainDestination
import ru.myitschool.work.ui.qr.scan.QrScanDestination
import ru.myitschool.work.utils.collectWhenStarted

@AndroidEntryPoint
class QrResultFragment: Fragment(R.layout.fragment_qr_result) {
  private var _binding: FragmentQrResultBinding? = null
  private val binding: FragmentQrResultBinding get() = _binding!!

  private val viewModel: QrResultViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    _binding = FragmentQrResultBinding.bind(view)
    subscribe()
    binding.close.setOnClickListener { findNavController().navigateUp() }

    findNavController().currentBackStackEntry?.savedStateHandle?.let {
      val data = it.get<Bundle>(QrScanDestination.REQUEST_KEY)?.getString("key_qr")
      val username = findNavController().currentBackStackEntry?.toRoute<MainDestination>()?.username
      username?.let { user ->
        viewModel.tryParseData(user, data)
      }
    }
  }

  private fun subscribe() {
    viewModel.result.collectWhenStarted(this) { data ->
      binding.result.setText(when (data) {
        QrResultViewModel.QrResultSource.SUCCESS -> R.string.success_result
        QrResultViewModel.QrResultSource.FAILURE -> R.string.failure_result
        QrResultViewModel.QrResultSource.CANCEL -> R.string.cancel_result
      })
    }
  }
}