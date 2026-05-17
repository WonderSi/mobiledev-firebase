package com.example.mobiledev_firebase.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mobiledev_firebase.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profile.collect { data ->
                if (data.isEmpty()) {
                    binding.tvName.text = "—"
                    binding.tvEmail.text = "—"
                    binding.tvFcmToken.text = "—"
                    binding.tvUpdatedAt.text = "—"
                } else {
                    binding.tvName.text = data["name"]?.toString() ?: "—"
                    binding.tvEmail.text = data["email"]?.toString() ?: "—"
                    binding.tvFcmToken.text = data["fcmToken"]?.toString() ?: "—"
                    binding.tvUpdatedAt.text = data["updatedAt"]?.toString() ?: "—"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
