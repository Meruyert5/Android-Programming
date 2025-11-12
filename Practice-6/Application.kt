package com.example.practice1

import android.app.Application

class ProfileApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val userRepository: UserRepository by lazy {
        UserRepository(
            userProfileDao = database.userProfileDao(),
            userApiService = RetrofitClient.instance
        )
    }
}