package com.example.mobileedv_firebase.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mobileedv_firebase.R
import com.example.mobileedv_firebase.databinding.FragmentLoginBinding
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    private lateinit var yandexAuthLauncher: ActivityResultLauncher<YandexAuthLoginOptions>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val yandexSdk = YandexAuthSdk.create(YandexAuthOptions(requireContext()))
        @Suppress("UNCHECKED_CAST")
        val typedContract = yandexSdk.contract as ActivityResultContract<YandexAuthLoginOptions, YandexAuthResult>
        yandexAuthLauncher = registerForActivityResult(typedContract) { result ->
            when (result) {
                is YandexAuthResult.Success -> viewModel.onYandexSuccess(result.token.value)
                is YandexAuthResult.Failure -> viewModel.onAuthError(result.exception.message ?: "Яндекс: ошибка авторизации")
                YandexAuthResult.Cancelled -> viewModel.onAuthCancelled()
                null -> viewModel.onAuthCancelled()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isAlreadyLoggedIn()) {
            navigateToMain()
            return
        }

        binding.btnLoginVk.setOnClickListener {
            viewModel.startLoading()
            VKID.instance.authorize(
                lifecycleOwner = viewLifecycleOwner,
                callback = object : VKIDAuthCallback {
                    override fun onAuth(accessToken: AccessToken) {
                        viewModel.onVkSuccess(
                            accessToken = accessToken.token,
                            userId = accessToken.userID,
                            email = null
                        )
                    }
                    override fun onFail(fail: VKIDAuthFail) {
                        if (fail is VKIDAuthFail.Canceled) {
                            viewModel.onAuthCancelled()
                        } else {
                            viewModel.onAuthError(fail.description)
                        }
                    }
                }
            )
        }

        binding.btnLoginYandex.setOnClickListener {
            viewModel.startLoading()
            yandexAuthLauncher.launch(YandexAuthLoginOptions())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is LoginUiState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        binding.btnLoginVk.isEnabled = true
                        binding.btnLoginYandex.isEnabled = true
                    }
                    is LoginUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnLoginVk.isEnabled = false
                        binding.btnLoginYandex.isEnabled = false
                    }
                    is LoginUiState.Success -> navigateToMain()
                    is LoginUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = state.message
                        binding.btnLoginVk.isEnabled = true
                        binding.btnLoginYandex.isEnabled = true
                    }
                }
            }
        }
    }

    private fun navigateToMain() {
        findNavController().navigate(R.id.action_login_to_habits)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
