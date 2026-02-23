package com.example.taskmanager.data.repository

import com.example.taskmanager.data.model.CreateTaskRequest
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.data.model.UpdateTaskRequest
import com.example.taskmanager.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository {

    private val apiService = RetrofitClient.apiService

    sealed class Result<out T> {
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
        object Loading : Result<Nothing>()
    }

    suspend fun getAllTasks(): Result<List<Task>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.getAllTasks()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Result.Success(body.data ?: emptyList())
                } else {
                    Result.Error(body?.message ?: "Unknown error")
                }
            } else {
                Result.Error("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }

    suspend fun createTask(title: String, description: String?): Result<Task> = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = CreateTaskRequest(title, description)
            val response = apiService.createTask(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    Result.Success(body.data)
                } else {
                    Result.Error(body?.message ?: "Failed to create task")
                }
            } else {
                Result.Error("Error: ${response.code()}")
            }

        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }

    suspend fun updateTask(id: Int, title: String, description: String?, isCompleted: Int): Result<Task> = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = UpdateTaskRequest(title, description, isCompleted)
            val response = apiService.updateTask(id, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    Result.Success(body.data)
                } else {
                    Result.Error(body?.message ?: "Failed to update task")
                }
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }

    suspend fun deleteTask(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.deleteTask(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Result.Success(Unit)
                } else {
                    Result.Error(body?.message ?: "Failed to delete task")
                }
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }

    suspend fun toggleTaskComplete(id: Int): Result<Task> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.toggleTaskComplete(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    Result.Success(body.data)
                } else {
                    Result.Error(body?.message ?: "Failed to toggle task")
                }
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }
}