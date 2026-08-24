package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

data class Friend(
    val id: String,
    val name: String,
    val bio: String,
    val avatarUrl: String,
    val isOnline: Boolean
)

val mockFriends = listOf(
    Friend("1", "Alice Smith", "Coffee lover ☕️ | Traveler", "https://api.dicebear.com/7.x/avataaars/svg?seed=Alice", true),
    Friend("2", "Bob Jones", "Tech enthusiast. Always learning.", "https://api.dicebear.com/7.x/avataaars/svg?seed=Bob", false),
    Friend("3", "Charlie Brown", "Just living life.", "https://api.dicebear.com/7.x/avataaars/svg?seed=Charlie", true),
    Friend("4", "Diana Prince", "Photographer and designer.", "https://api.dicebear.com/7.x/avataaars/svg?seed=Diana", false),
    Friend("5", "Ethan Hunt", "Busy at work.", "https://api.dicebear.com/7.x/avataaars/svg?seed=Ethan", true)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    onNavigateToAddFriend: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Friends") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToAddFriend) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Add Friend")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(mockFriends.filter { !blockedUserIds.contains(it.id) }) { friend ->
                FriendItem(
                    friend = friend,
                    onClick = { onNavigateToChat(friend.id) }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 80.dp), color = MaterialTheme.colorScheme.surfaceVariant)
            }
        }
    }
}

@Composable
fun FriendItem(friend: Friend, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(56.dp)) {
            AsyncImage(
                model = friend.avatarUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
            if (friend.isOnline) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                        .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = friend.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = friend.bio,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.Chat,
                contentDescription = "Message",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
