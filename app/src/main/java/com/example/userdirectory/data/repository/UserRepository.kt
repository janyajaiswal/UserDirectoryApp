package com.example.userdirectory.data.repository

import com.example.userdirectory.data.local.UserDao
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.remote.ApiService
import com.example.userdirectory.data.remote.UserDto
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    // Offline-first: UI always reads from Room
    fun getUsersFlow(query: String): Flow<List<UserEntity>> {
        return if (query.isBlank()) {
            userDao.getUsers()
        } else {
            userDao.searchUsers(query)
        }
    }

    // Fetch from API and update Room
    suspend fun refreshUsers() {
        // Network call
        val usersFromApi: List<UserDto> = apiService.getUsers()
        val entities = usersFromApi.map { dto ->
            UserEntity(
                id = dto.id,
                name = dto.name,
                username = dto.username,
                email = dto.email,
                phone = dto.phone,
                website = dto.website
            )
        }

        // Replace DB content to match server
        userDao.clearUsers()
        userDao.insertUsers(entities)
    }
}
