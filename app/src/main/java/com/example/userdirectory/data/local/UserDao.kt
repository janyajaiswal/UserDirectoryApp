package com.example.userdirectory.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // Requirement #3: UI must read FROM ROOM ONLY (Single Source of Truth)
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsers(): Flow<List<UserEntity>>

    // Requirement #5: Local search (NO API call)
    @Query("""
        SELECT * FROM users
        WHERE name LIKE '%' || :query || '%'
           OR email LIKE '%' || :query || '%'
        ORDER BY id ASC
    """)
    fun searchUsers(query: String): Flow<List<UserEntity>>

    // Requirement #1: Store users in Room using REPLACE to update existing rows
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    // Used when refreshing from API
    @Query("DELETE FROM users")
    suspend fun clearUsers()
}

