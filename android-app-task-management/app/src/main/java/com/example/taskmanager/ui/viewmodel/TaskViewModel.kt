package com.example.taskmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val repository = TaskRepository()

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> = _operationSuccess

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            when (val result = repository.getAllTasks()) {
                is TaskRepository.Result.Success -> {
                    _tasks.value = result.data
                }
                is TaskRepository.Result.Error -> {
                    _error.value = result.message
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    fun createTask(title: String, description: String?) {
        viewModelScope.launch {
            _isLoading.value = true

            when (val result = repository.createTask(title, description)) {
                is TaskRepository.Result.Success -> {
                    loadTasks() // Refresh list
                    _operationSuccess.value = true
                }
                is TaskRepository.Result.Error -> {
                    _error.value = result.message
                    _operationSuccess.value = false
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    fun updateTask(id: Int, title: String, description: String?, isCompleted: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true

            when (val result = repository.updateTask(id, title, description, isCompleted)) {
                is TaskRepository.Result.Success -> {
                    loadTasks()
                    _operationSuccess.value = true
                }
                is TaskRepository.Result.Error -> {
                    _error.value = result.message
                    _operationSuccess.value = false
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true

            when (val result = repository.deleteTask(id)) {
                is TaskRepository.Result.Success -> {
                    loadTasks()
                    _operationSuccess.value = true
                }
                is TaskRepository.Result.Error -> {
                    _error.value = result.message
                    _operationSuccess.value = false
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    fun toggleTaskComplete(id: Int) {
        viewModelScope.launch {
            when (val result = repository.toggleTaskComplete(id)) {
                is TaskRepository.Result.Success -> {
                    loadTasks()
                }
                is TaskRepository.Result.Error -> {
                    _error.value = result.message
                }
                else -> {}
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearOperationSuccess() {
        _operationSuccess.value = false
    }
}