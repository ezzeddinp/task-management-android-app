package com.example.taskmanager

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.databinding.ActivityAddTaskBinding
import com.example.taskmanager.ui.viewmodel.TaskViewModel

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private val viewModel: TaskViewModel by viewModels()
    
    private var isEditMode = false
    private var taskId: Int = -1
    private var currentStatus: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cek apakah ada data task yang dikirim (berarti mode edit)
        val taskIdExtra = intent.getIntExtra("TASK_ID", -1)
        if (taskIdExtra != -1) {
            isEditMode = true
            taskId = taskIdExtra
            val title = intent.getStringExtra("TASK_TITLE") ?: ""
            val description = intent.getStringExtra("TASK_DESC")
            currentStatus = intent.getIntExtra("TASK_STATUS", 0)

            binding.etTitle.setText(title)
            binding.etDescription.setText(description)
            supportActionBar?.title = "Edit Task"
            binding.btnSave.text = "Update Task"
        } else {
            supportActionBar?.title = "Add New Task"
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnSave.isEnabled = !isLoading
            binding.btnSave.text = if (isLoading) "Saving..." else if (isEditMode) "Update Task" else "Save Task"
        }

        viewModel.operationSuccess.observe(this) { success ->
            if (success) {
                val msg = if (isEditMode) "Task updated!" else "Task created!"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()

            if (title.isEmpty()) {
                binding.etTitle.error = "Title is required"
                return@setOnClickListener
            }

            if (isEditMode) {
                viewModel.updateTask(
                    id = taskId,
                    title = title,
                    description = description.ifEmpty { null },
                    isCompleted = currentStatus
                )
            } else {
                viewModel.createTask(
                    title = title,
                    description = description.ifEmpty { null }
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}