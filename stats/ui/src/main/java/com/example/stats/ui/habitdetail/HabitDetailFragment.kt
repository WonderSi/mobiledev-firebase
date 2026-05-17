package com.example.stats.ui.habitdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.stats.ui.databinding.FragmentHabitDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HabitDetailFragment : Fragment() {

    private var _binding: FragmentHabitDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HabitDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHabitDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val habitId = arguments?.getLong("habitId") ?: return
        viewModel.loadStats(habitId)

        binding.btnDelete.setOnClickListener {
            viewModel.deleteHabit(habitId) { findNavController().popBackStack() }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    state.error?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
                    state.stats?.let { stats ->
                        binding.tvTitle.text = stats.habit.title
                        binding.tvDescription.text = stats.habit.description
                        binding.tvCurrentStreak.text = "Current streak: ${stats.currentStreak} days"
                        binding.tvLongestStreak.text = "Longest streak: ${stats.longestStreak} days"
                        binding.tvTotalCompleted.text = "Total completed: ${stats.totalCompleted} times"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
