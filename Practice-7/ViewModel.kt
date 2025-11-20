package com.example.practice1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _followers = MutableStateFlow(com.example.practice1.initialFollowers)
    val followers: StateFlow<List<Follower>> = _followers.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage.asStateFlow()

    private val _showSnackbar = MutableStateFlow(false)
    val showSnackbar: StateFlow<Boolean> = _showSnackbar.asStateFlow()

    init {
        loadLocalUserProfile()
    }

    private fun loadLocalUserProfile(userId: Int = 1) {
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
            showMessage("Name updated!")
        }
    }

    fun updateBio(newBio: String) {
        viewModelScope.launch {
            val currentProfile = _userProfile.value ?: return@launch
            val updatedProfile = currentProfile.copy(bio = newBio)
            userRepository.updateUserProfile(updatedProfile)
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