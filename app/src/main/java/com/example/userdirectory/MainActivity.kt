package com.example.userdirectory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.userdirectory.data.local.UserDatabase
import com.example.userdirectory.data.remote.ApiClient
import com.example.userdirectory.data.repository.UserRepository
import com.example.userdirectory.ui.UserViewModel
import com.example.userdirectory.ui.components.UserListScreen
import com.example.userdirectory.ui.theme.UserDirectoryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create DB, API, Repository, ViewModel (very lightweight app-scale DI)
        val database = UserDatabase.getInstance(applicationContext)
        val apiService = ApiClient.apiService
        val repository = UserRepository(apiService, database.userDao())

        val viewModelFactory = UserViewModel.Factory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]

        enableEdgeToEdge()

        setContent {
            UserDirectoryTheme {
                UserListScreen(viewModel = viewModel)
            }
        }
    }
}
