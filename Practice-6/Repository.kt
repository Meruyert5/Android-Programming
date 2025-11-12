package com.example.practice1

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository(
    private val userProfileDao: UserProfileDao,
    private val userApiService: UserApiService
) {
    suspend fun refreshUserData(userId: Int = 1): Result<UserProfile> {
        return try {
            val apiUsers = userApiService.getUsers()
            val apiUser = apiUsers.firstOrNull { it.id == userId } ?: apiUsers.first()

            val userProfile = UserProfile(
                id = apiUser.id,
                name = apiUser.name,
                username = apiUser.username,
                email = apiUser.email,
                phone = apiUser.phone,
                website = apiUser.website,
                companyName = apiUser.company?.name ?: "Unknown Company",
                catchPhrase = apiUser.company?.catchPhrase ?: "No catchphrase",
                bio = "${apiUser.address?.city ?: "Unknown City"} • ${apiUser.company?.bs ?: "Professional"}"
            )

            userProfileDao.insertUserProfile(userProfile)

            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserProfile(userId: Int): Flow<UserProfile?> = flow {
        val localUser = userProfileDao.getUserProfile(userId)
        emit(localUser)

        if (localUser == null) {
            refreshUserData(userId).onSuccess { userProfile ->
                emit(userProfile)
            }.onFailure {
                emit(null)
            }
        }
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.updateUserProfile(userProfile)
    }
}