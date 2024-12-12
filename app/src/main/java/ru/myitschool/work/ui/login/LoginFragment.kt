package ru.myitschool.work.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.databinding.FragmentLoginBinding
import ru.myitschool.work.utils.collectWhenStarted
import ru.myitschool.work.utils.visibleOrGone
import ru.myitschool.work.utils.TextChangedListener


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!




    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.username.addTextChangedListener(TextChangedListener(binding))
        val texte = binding.username.text.toString()
        binding.login.setOnClickListener { viewModel.userClicked(texte) }

        subscribe()

    }



    private fun subscribe() {
        viewModel.state.collectWhenStarted(this) { state ->
            binding.loading.visibleOrGone(state)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}