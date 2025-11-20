package com.example.practice1

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val userApiService: UserApiService
) {
    suspend fun refreshUserData(userId: Int = 1): Result<UserProfile> {
        return try {
            val apiUser = userApiService.getUser(userId)

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

    fun getUserProfile(userId: Int): Flow<UserProfile?> {
        return userProfileDao.getUserProfile(userId)
    }

    suspend fun ensureUserProfileExists(userId: Int = 1) {
        val existingProfile = userProfileDao.getUserProfile(userId).first()
        if (existingProfile == null) {
            refreshUserData(userId)
        }
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.updateUserProfile(userProfile)
    }

    suspend fun getPosts(): List<Post> {
        return try {
            userApiService.getPosts()
        } catch (e: Exception) {
            emptyList()
        }
    }
}