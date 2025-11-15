package com.example.userdirectory.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UserUiState(
    val users: List<UserEntity> = emptyList(),
    val searchQuery: String = ""
)

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Requirement #5: Debounce search to prevent spamming DB
    private val debouncedQuery = _searchQuery
        .debounce(250)
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            ""
        )

    // Requirement #3: UI State = Room Flow ONLY
    val uiState: StateFlow<UserUiState> =
        debouncedQuery
            .flatMapLatest { query ->
                Log.d("DEBUG_VM", "UI observing Room Flow. Query=\"$query\"")
                repository.getUsersFlow(query)
            }
            .map { users ->
                UserUiState(
                    users = users,
                    searchQuery = _searchQuery.value
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                UserUiState()
            )

    init {
        Log.d("DEBUG_VM", "ViewModel created — App launched")

        viewModelScope.launch {
            // Requirement #4: Offline-first — Try API but show Room first
            Log.d("DEBUG_VM", "OFFLINE-FIRST: Display Room data immediately")
            try {
                repository.refreshUsers()
                Log.d("DEBUG_VM", "API refresh completed")
            } catch (e: Exception) {
                Log.e("DEBUG_VM", "API refresh failed, using cached data: ${e.message}")
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        Log.d("DEBUG_VM", "Search query updated: \"$newQuery\"")
        _searchQuery.value = newQuery
    }

    // Factory for creating ViewModel with repository
    class Factory(
        private val repository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(repository) as T
        }
    }
}
