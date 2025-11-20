package com.example.practice1

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val companyName: String,
    val catchPhrase: String,
    val bio: String = "",
    val avatarResId: Int = R.drawable.ic_avatar
)

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = :userId")
    fun getUserProfile(userId: Int): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    @Update
    suspend fun updateUserProfile(userProfile: UserProfile)

    @Query("DELETE FROM user_profile")
    suspend fun deleteAllUserProfiles()

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getFirstUserProfile(): UserProfile?
}

@Database(
    entities = [UserProfile::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
}