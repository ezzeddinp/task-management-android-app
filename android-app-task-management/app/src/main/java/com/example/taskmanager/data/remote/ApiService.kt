package com.example.taskmanager.data.remote

import com.example.taskmanager.data.model.ApiResponse
import com.example.taskmanager.data.model.CreateTaskRequest
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.data.model.UpdateTaskRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("tasks")
    suspend fun getAllTasks(): Response<ApiResponse<List<Task>>>

    @GET("tasks/{id}")
    suspend fun getTaskById(@Path("id") id: Int): Response<ApiResponse<Task>>

    @POST("tasks")
    suspend fun createTask(@Body request: CreateTaskRequest): Response<ApiResponse<Task>>

    @PUT("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: Int,
        @Body request: UpdateTaskRequest
    ): Response<ApiResponse<Task>>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<ApiResponse<Unit>>

    @PATCH("tasks/{id}/toggle")
    suspend fun toggleTaskComplete(@Path("id") id: Int): Response<ApiResponse<Task>>
}