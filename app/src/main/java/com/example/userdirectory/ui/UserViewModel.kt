package com.example.userdirectory.ui

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

    // Debounced search trigger (fixes keyboard lag!)
    private val debouncedQuery = _searchQuery
        .debounce(250L)
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            ""
        )

    val uiState = debouncedQuery
        .flatMapLatest { query ->
            repository.getUsersFlow(query)
        }
        .map { users ->
            UserUiState(users = users, searchQuery = _searchQuery.value)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserUiState()
        )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    init {
        viewModelScope.launch {
            try { repository.refreshUsers() }
            catch (_: Exception) {}
        }
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
