package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.ui.adapter.TaskAdapter
import com.example.taskmanager.ui.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskClick = { task ->
                showTaskDetails(task)
            },
            onTaskToggle = { task ->
                viewModel.toggleTaskComplete(task.id)
            },
            onTaskDelete = { task ->
                showDeleteConfirmation(task)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }
    }

    private fun setupObservers() {
        viewModel.tasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
            binding.emptyState.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }

        viewModel.operationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Operation successful", Toast.LENGTH_SHORT).show()
                viewModel.clearOperationSuccess()
            }
        }
    }

    private fun setupListeners() {
        binding.fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadTasks()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun showTaskDetails(task: Task) {
        AlertDialog.Builder(this)
            .setTitle(task.title)
            .setMessage("""
                Description: ${task.description ?: "No description"}
                Status: ${if (task.isCompleted) "Completed" else "Pending"}
                Created: ${task.createdAt}
            """.trimIndent())
            .setPositiveButton("OK", null)
            .setNeutralButton("Toggle Status") { _, _ ->
                viewModel.toggleTaskComplete(task.id)
            }
            .show()
    }

    private fun showDeleteConfirmation(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete '${task.title}'?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteTask(task.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTasks()
    }
}