// File: `app/src/main/java/com/example/userdirectory/data/repository/UserRepository.kt`
package com.example.userdirectory.data.repository

import android.util.Log
import com.example.userdirectory.data.local.UserDao
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.remote.ApiService
import com.example.userdirectory.data.remote.UserDto
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val apiService: ApiService,
    private val userDao: UserDao
) {

    // Requirement #3: UI must always read from Room using Flow
    fun getUsersFlow(query: String): Flow<List<UserEntity>> {
        Log.d("DEBUG_FLOW", "Room Flow emitting… query = \"$query\"")
        return if (query.isBlank()) userDao.getUsers()
        else userDao.searchUsers(query)
    }

    // Requirement #1 + #4: Fetch from API → Update Room (offline-first)
    suspend fun refreshUsers() {
        Log.d("DEBUG_API", "Attempting API fetch…")

        try {
            // Step 2: Try fetch fresh data
            val usersFromApi = apiService.getUsers()
            Log.d("DEBUG_API", "API fetch SUCCESS: ${usersFromApi.size} users")

            // Convert
            val entities = usersFromApi.map { dto: UserDto ->
                UserEntity(
                    id = dto.id,
                    name = dto.name,
                    username = dto.username,
                    email = dto.email,
                    phone = dto.phone,
                    website = dto.website
                )
            }

            // Step 3: Update Room
            userDao.clearUsers()
            userDao.insertUsers(entities)

            Log.d("DEBUG_DB", "Room updated with latest API data")

        } catch (e: Exception) {
            Log.e("DEBUG_API", "API fetch FAILED, using cached data: ${e.message}")
            // Step 5: Offline — silently rely on ROOM
        }
    }
}
