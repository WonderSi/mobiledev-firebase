package com.example.habits.ui.habitlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habits.domain.model.Habit
import com.example.habits.ui.databinding.ItemHabitBinding

data class HabitListItem(val habit: Habit, val isCompletedToday: Boolean)

class HabitAdapter(
    private val onCheckChanged: (Long) -> Unit,
    private val onItemClick: (Long) -> Unit
) : ListAdapter<HabitListItem, HabitAdapter.HabitViewHolder>(DIFF_CALLBACK) {

    inner class HabitViewHolder(private val binding: ItemHabitBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HabitListItem) {
            binding.tvTitle.text = item.habit.title
            binding.tvDescription.text = item.habit.description
            binding.colorIndicator.setBackgroundColor(item.habit.color)
            binding.cbCompleted.setOnCheckedChangeListener(null)
            binding.cbCompleted.isChecked = item.isCompletedToday
            binding.cbCompleted.setOnCheckedChangeListener { _, _ -> onCheckChanged(item.habit.id) }
            binding.root.setOnClickListener { onItemClick(item.habit.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HabitListItem>() {
            override fun areItemsTheSame(old: HabitListItem, new: HabitListItem) =
                old.habit.id == new.habit.id
            override fun areContentsTheSame(old: HabitListItem, new: HabitListItem) =
                old == new
        }
    }
}
