package com.example.taskmanager.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.data.model.Task
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onTaskToggle: (Task) -> Unit,
    private val onTaskDelete: (Task) -> Unit,
    private val onTaskEdit: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val checkBoxComplete: CheckBox = itemView.findViewById(R.id.checkBoxComplete)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)

        fun bind(task: Task) {
            tvTitle.text = task.title
            tvDescription.text = task.description ?: "No description"
            tvDescription.visibility = if (task.description.isNullOrEmpty()) View.GONE else View.VISIBLE

            // PRIORITASKAN JAM UPDATE, JIKA GAK ADA PAKAI JAM CREATE
            val displayDate = if (!task.updatedAt.isNullOrEmpty() && task.updatedAt != task.createdAt) {
                "Updated: ${task.updatedAt}"
            } else {
                task.createdAt
            }

            // PERBAIKAN FORMAT JAM: Konversi dari UTC ke Waktu Lokal
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            
            val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getDefault()

            tvDate.text = try {
                // Bersihkan string date jika ada prefix "Updated: "
                val rawDate = displayDate.replace("Updated: ", "")
                val date = inputFormat.parse(rawDate)
                val formatted = outputFormat.format(date!!)
                if (displayDate.startsWith("Updated: ")) "Updated: $formatted" else formatted
            } catch (e: Exception) {
                try {
                    val fallbackInput = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val rawDate = displayDate.replace("Updated: ", "")
                    val date = fallbackInput.parse(rawDate)
                    val formatted = outputFormat.format(date!!)
                    if (displayDate.startsWith("Updated: ")) "Updated: $formatted" else formatted
                } catch (e2: Exception) {
                    displayDate
                }
            }

            val isCompleted = task.isCompleted == 1
            checkBoxComplete.isChecked = isCompleted
            tvTitle.paint.isStrikeThruText = isCompleted
            tvTitle.alpha = if (isCompleted) 0.6f else 1.0f

            itemView.setOnClickListener { onTaskClick(task) }
            checkBoxComplete.setOnClickListener { onTaskToggle(task) }
            btnDelete.setOnClickListener { onTaskDelete(task) }
            btnEdit.setOnClickListener { onTaskEdit(task) }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}