package com.example.screens

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import com.example.ui.theme.BottomBarBackground
import com.example.ui.theme.Success

data class ChatSummary(
    val id: String,
    val name: String,
    val lastMessage: String,
    val senderPrefix: String? = null,
    val time: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val isDraft: Boolean = false,
    val avatarUrl: String,
    val hasUnreadIndicator: Boolean = false,
    val backgroundColor: Color = Color.Transparent,
    val opacity: Float = 1f,
    val mobileNumber: String = ""
)

val mockChats = listOf(
    ChatSummary(
        id = "1", 
        name = "Sarah Miller", 
        lastMessage = "Wait, did you hear about the...", 
        time = "10:42 AM", 
        unreadCount = 2, 
        isOnline = true,
        avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah",
        hasUnreadIndicator = true,
        backgroundColor = Color(0xFF3B0764).copy(alpha = 0.6f),
        mobileNumber = "+1234567890"
    ),
    ChatSummary(
        id = "2", 
        name = "Design Team \uD83C\uDFA8", 
        senderPrefix = "Alex: ",
        lastMessage = "The new icons look amazing!", 
        time = "09:15 AM", 
        unreadCount = 0,
        avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=Team"
    ),
    ChatSummary(
        id = "3", 
        name = "Marcus Chen",
        senderPrefix = "Draft: ",
        lastMessage = "I'll be there in 5 mins...", 
        time = "Yesterday", 
        unreadCount = 0,
        isDraft = true,
        avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=Marcus",
        opacity = 0.7f,
        mobileNumber = "+1987654321"
    ),
    ChatSummary(
        id = "4", 
        name = "Mom", 
        lastMessage = "\uD83D\uDCF7 Photo", 
        time = "Yesterday", 
        unreadCount = 0,
        avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=Mom",
        mobileNumber = "+1122334455"
    )
)

@Composable
fun ChatListScreen(
    onNavigateToChat: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAddFriend: () -> Unit,
    onNavigateToFriends: () -> Unit
) {
    var selectedTab by androidx.compose.runtime.remember { androidx.compose.runtime.mutableIntStateOf(0) }
    var searchQuery by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var isSearchActive by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { _ -> }
    )

    LaunchedEffect(Unit) {
        val permissions = mutableListOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 8.dp)
            ) {
                if (isSearchActive) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search name or number...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        },
                        trailingIcon = {
                            IconButton(onClick = { 
                                isSearchActive = false
                                searchQuery = ""
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Close Search")
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Gossip",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = if (selectedTab == 0) "3 unread conversations" else "Share your moments",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = { isSearchActive = true }) {
                                Text("🔍", fontSize = 24.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable(onClick = onNavigateToProfile),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "JD",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NavPill(
                        text = "Messages", 
                        isSelected = selectedTab == 0, 
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    NavPill(
                        text = "Status", 
                        isSelected = selectedTab == 1, 
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1f)
                    )
                    NavPill(
                        text = "Groups", 
                        isSelected = selectedTab == 2, 
                        onClick = { selectedTab = 2 },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    if (selectedTab == 0) {
                        onNavigateToAddFriend()
                    } else if (selectedTab == 2) {
                        // In a real app this would go to a create group screen
                        onNavigateToAddFriend()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(if (selectedTab == 1) "📷" else if (selectedTab == 2) "👥" else "✍️", fontSize = 24.sp)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    val filteredChats = mockChats.filter {
                        it.name.contains(searchQuery, ignoreCase = true) || 
                        it.mobileNumber.contains(searchQuery, ignoreCase = true)
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filteredChats) { chat ->
                            ChatItem(chat = chat, onClick = { onNavigateToChat(chat.id) })
                        }
                    }
                }
                1 -> {
                    StatusTab()
                }
                2 -> {
                    val mockGroups = listOf(
                        ChatSummary("g1", "Family Group", "Hello everyone!", "Someone", "10:00 AM", 5, false, false, "https://api.dicebear.com/7.x/avataaars/svg?seed=Family"),
                        ChatSummary("g2", "Weekend Trip", "Are we still on?", "Alice", "Yesterday", 2, false, false, "https://api.dicebear.com/7.x/avataaars/svg?seed=Trip"),
                        ChatSummary("g3", "Project Team", "I'll review the PR.", "You", "Mon", 0, false, false, "https://api.dicebear.com/7.x/avataaars/svg?seed=Team")
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(mockGroups) { group ->
                            ChatItem(chat = group, onClick = { onNavigateToChat(group.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavPill(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ChatItem(chat: ChatSummary, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(chat.backgroundColor)
            .clickable(onClick = onClick)
            .padding(12.dp)
            .alpha(chat.opacity),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(56.dp)) {
            AsyncImage(
                model = chat.avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            if (chat.isOnline) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Success)
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.name,
                    fontWeight = if (chat.hasUnreadIndicator) FontWeight.Bold else FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = chat.time,
                    fontSize = 11.sp,
                    fontWeight = if (chat.hasUnreadIndicator) FontWeight.Bold else FontWeight.Normal,
                    color = if (chat.hasUnreadIndicator) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (chat.senderPrefix != null) {
                        Text(
                            text = chat.senderPrefix,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (chat.isDraft) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                            fontStyle = if (chat.isDraft) FontStyle.Italic else FontStyle.Normal
                        )
                    }
                    Text(
                        text = chat.lastMessage,
                        fontSize = 14.sp,
                        fontWeight = if (chat.hasUnreadIndicator) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (chat.hasUnreadIndicator) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontStyle = if (chat.isDraft) FontStyle.Italic else FontStyle.Normal
                    )
                }
                
                if (chat.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .height(18.dp)
                            .defaultMinSize(minWidth = 18.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = chat.unreadCount.toString(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar() {
    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BottomBarBackground)
                .padding(horizontal = 32.dp, vertical = 12.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem("Chats", "💬", true)
            BottomNavItem("Starred", "⭐", false)
            BottomNavItem("Friends", "👥", false)
        }
    }
}

@Composable
fun BottomNavItem(label: String, icon: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable { /* Select tab */ }
    ) {
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(32.dp)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 20.sp)
        }
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
