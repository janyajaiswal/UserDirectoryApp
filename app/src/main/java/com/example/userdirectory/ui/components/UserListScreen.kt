package com.example.userdirectory.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.ui.UserUiState
import com.example.userdirectory.ui.UserViewModel

@Composable
fun UserListScreen(viewModel: UserViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("User Directory")

        Spacer(modifier = Modifier.height(8.dp))

        // Important: keep TextField controlled by its own state
        SearchBar(
            query = viewModel.searchQuery.collectAsState().value,
            onQueryChange = viewModel::onSearchQueryChange
        )

        Spacer(modifier = Modifier.height(8.dp))

        // VERY IMPORTANT:
        // Wrap list in its own key(), so list recomposes independently
        key(uiState.users.hashCode()) {
            UserList(users = uiState.users)
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    // Keep internal cursor/typing state isolated
    var localQuery by remember { mutableStateOf(query) }

    // Sync only when external query changes (not every keystroke)
    LaunchedEffect(query) {
        if (query != localQuery) {
            localQuery = query
        }
    }

    OutlinedTextField(
        value = localQuery,
        onValueChange = { text ->
            localQuery = text
            onQueryChange(text)
        },
        label = { Text("Search by name or email") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}


@Composable
fun UserList(
    users: List<UserEntity>
) {
    if (users.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No users to display")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                UserCard(user = user)
            }
        }
    }
}

@Composable
fun UserCard(
    user: UserEntity
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "ID: ${user.id}")
            Text(text = "Name: ${user.name}")
            Text(text = "Email: ${user.email}")
            Text(text = "Phone: ${user.phone}")
        }
    }
}
