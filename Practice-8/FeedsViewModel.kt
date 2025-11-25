package com.example.practice1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class PostWithEngagement(
    val post: Post,
    var likes: Int = 0,
    val comments: MutableList<Comment> = mutableListOf()
)

data class Comment(
    val id: Int,
    val userName: String,
    val text: String,
    val timestamp: String
)

@HiltViewModel
class FeedsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<PostWithEngagement>>(emptyList())
    val posts: StateFlow<List<PostWithEngagement>> = _posts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val apiPosts = userRepository.getPosts()
                if (apiPosts.isNotEmpty()) {
                    _posts.value = apiPosts.map { post ->
                        PostWithEngagement(
                            post = post,
                            likes = (0..200).random(),
                            comments = mutableListOf()
                        )
                    }
                } else {
                    _posts.value = getSamplePosts()
                }
            } catch (e: Exception) {
                _posts.value = getSamplePosts()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun likePost(postId: Int) {
        _posts.value = _posts.value.map { postWithEngagement ->
            if (postWithEngagement.post.id == postId) {
                postWithEngagement.copy(likes = postWithEngagement.likes + 1)
            } else {
                postWithEngagement
            }
        }
    }

    fun addComment(postId: Int, commentText: String) {
        _posts.value = _posts.value.map { postWithEngagement ->
            if (postWithEngagement.post.id == postId) {
                val newComment = Comment(
                    id = postWithEngagement.comments.size + 1,
                    userName = "You",
                    text = commentText,
                    timestamp = "Just now"
                )
                postWithEngagement.copy(
                    comments = (postWithEngagement.comments + newComment).toMutableList()
                )
            } else {
                postWithEngagement
            }
        }
    }
    private fun getSamplePosts(): List<PostWithEngagement> {
        return listOf(
            PostWithEngagement(
                post = Post(1, 1, "Exploring Compose", "Just started learning Jetpack Compose and it's amazing! The declarative style is a game-changer. #AndroidDev #Compose"),
                likes = 42,
                comments = mutableListOf(
                    Comment(1, "Alex", "Looks amazing!", "2 hours ago"),
                    Comment(2, "Sarah", "Wish I was there!", "1 hour ago")
                )
            ),
            PostWithEngagement(
                post = Post(2, 2, "Learning Compose", "Working on my Android skills with Jetpack Compose!  💻  It's amazing how declarative UI makes development so much easier."),
                likes = 89,
                comments = mutableListOf(
                    Comment(3, "Mike", "Compose is awesome!", "5 hours ago"),
                    Comment(4, "Emma", "Great progress!", "3 hours ago")
                )
            ),
            PostWithEngagement(
                post = Post(3, 3, "Coffee Time", "Nothing better than a good cup of coffee to start the day!  ☕  Perfect for coding sessions."),
                likes = 23,
                comments = mutableListOf(
                    Comment(5, "David", "I need one too!", "1 hour ago")
                )
            ),
            PostWithEngagement(
                post = Post(4, 4, "New Project", "Started working on a new mobile app project. Can't wait to share more details soon!  🚀 "),
                likes = 156,
                comments = mutableListOf(
                    Comment(6, "Lisa", "Excited to see it!", "8 hours ago"),
                    Comment(7, "John", "What's the tech stack?", "6 hours ago")
                )
            )
        )
    }
}