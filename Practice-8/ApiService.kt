package com.example.practice1

import retrofit2.http.GET
import retrofit2.http.Path

data class ApiUser(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val company: Company?,
    val address: Address?
)

data class Company(
    val name: String,
    val catchPhrase: String,
    val bs: String
)

data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: Geo
)

data class Geo(
    val lat: String,
    val lng: String
)

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

interface UserApiService {
    @GET("users")
    suspend fun getUsers(): List<ApiUser>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): ApiUser

    @GET("posts")
    suspend fun getPosts(): List<Post>
}