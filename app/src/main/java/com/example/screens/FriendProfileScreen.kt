package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileScreen(
    chatId: String,
    onNavigateBack: () -> Unit,
    onNavigateToCall: (Boolean) -> Unit
) {
    val chat = mockChats.find { it.id == chatId }
    if (chat == null) {
        onNavigateBack()
        return
    }

    var isStarred by remember { mutableStateOf(chat.isStarred) }
    var isBlocked by remember { mutableStateOf(blockedUserIds.contains(chatId)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = chat.avatarUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = chat.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = if (chat.mobileNumber.isNotEmpty()) chat.mobileNumber else "+1 234 567 8900",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { onNavigateToCall(false) },
                        modifier = Modifier
                            .size(56.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Audio", style = MaterialTheme.typography.bodySmall)
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { onNavigateToCall(true) },
                        modifier = Modifier
                            .size(56.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Video", style = MaterialTheme.typography.bodySmall)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = "Hey there! I am using this app.",
                onValueChange = {},
                readOnly = true,
                label = { Text("Status") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                shape = MaterialTheme.shapes.medium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            ListItem(
                headlineContent = { Text(if (isStarred) "Starred Contact" else "Star Contact", fontWeight = FontWeight.Bold) },
                leadingContent = { 
                    Icon(
                        if (isStarred) Icons.Default.Star else Icons.Default.StarBorder, 
                        contentDescription = null, 
                        tint = if (isStarred) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurfaceVariant
                    ) 
                },
                modifier = Modifier.clickable {
                    isStarred = !isStarred
                    val index = mockChats.indexOf(chat)
                    if (index != -1) {
                        mockChats[index] = chat.copy(isStarred = isStarred)
                    }
                }
            )
            
            ListItem(
                headlineContent = { Text(if (isBlocked) "Unblock User" else "Block User", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error) },
                modifier = Modifier.clickable {
                    if (isBlocked) {
                        blockedUserIds.remove(chatId)
                    } else {
                        blockedUserIds.add(chatId)
                    }
                    isBlocked = !isBlocked
                }
            )
        }
    }
}
