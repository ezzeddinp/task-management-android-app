package com.example.taskmanager.utils

object Constants {
    // Untuk Android Emulator, gunakan 10.0.2.2 untuk mengakses localhost host
    // Untuk device fisik, gunakan IP address komputer Anda dalam satu network
    const val BASE_URL = "http://10.0.2.2:3000/api/"

    // Timeout durations
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}