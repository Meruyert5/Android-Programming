package com.example.practice1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _followers = MutableStateFlow(initialFollowers)
    val followers: StateFlow<List<Follower>> = _followers.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage.asStateFlow()

    private val _showSnackbar = MutableStateFlow(false)
    val showSnackbar: StateFlow<Boolean> = _showSnackbar.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile(userId: Int = 1) {
        viewModelScope.launch {
            userRepository.getUserProfile(userId).collect { profile ->
                _userProfile.value = profile
            }
        }
    }

    fun refreshUserData(userId: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.refreshUserData(userId)
            _isLoading.value = false

            result.onSuccess { userProfile ->
                _userProfile.value = userProfile
                showMessage("Profile updated from API!")
            }.onFailure { exception ->
                showMessage("Failed to update: ${exception.message}")
            }
        }
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            val currentProfile = _userProfile.value ?: return@launch
            val updatedProfile = currentProfile.copy(name = newName)
            userRepository.updateUserProfile(updatedProfile)
            _userProfile.value = updatedProfile
            showMessage("Name updated!")
        }
    }

    fun updateBio(newBio: String) {
        viewModelScope.launch {
            val currentProfile = _userProfile.value ?: return@launch
            val updatedProfile = currentProfile.copy(bio = newBio)
            userRepository.updateUserProfile(updatedProfile)
            _userProfile.value = updatedProfile
            showMessage("Bio updated!")
        }
    }

    fun addFollower(follower: Follower) {
        _followers.value = _followers.value + follower
    }

    fun removeFollower(followerId: Int) {
        _followers.value = _followers.value.filter { it.id != followerId }
    }

    private fun showMessage(message: String) {
        _snackbarMessage.value = message
        _showSnackbar.value = true
    }

    fun resetSnackbar() {
        _showSnackbar.value = false
    }
}

class ProfileViewModelFactory(
    private val userRepository: UserRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}