package com.example.taskmanager.data.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("is_completed")
    val isCompleted: Boolean,

    @SerializedName("created_at")
    val createdAt: String
)

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: T?,

    @SerializedName("timestamp")
    val timestamp: String
)

data class CreateTaskRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("is_completed")
    val isCompleted: Boolean = false
)

data class UpdateTaskRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("is_completed")
    val isCompleted: Boolean
)