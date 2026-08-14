package com.example.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySettingsScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("privacy_settings", Context.MODE_PRIVATE) }

    var isLiveLocationShared by remember { mutableStateOf(sharedPrefs.getBoolean("live_location", false)) }
    var isChatVisible by remember { mutableStateOf(sharedPrefs.getBoolean("chat_visible", true)) }
    var isPrivateChatEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("private_chat", true)) }
    var isPublicProfile by remember { mutableStateOf(sharedPrefs.getBoolean("public_profile", true)) }
    var isBirthdayNotificationEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("birthday_notifications", true)) }
    var statusPrivacy by remember { mutableStateOf(sharedPrefs.getString("status_privacy", "Everyone") ?: "Everyone") }
    var isStatusPrivacyMenuExpanded by remember { mutableStateOf(false) }

    fun updateSetting(key: String, value: Boolean) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Settings") },
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
            
            Text(
                text = "Control Your Footprint",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )
            
            Text(
                text = "Manage how others see your activity and location.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 32.dp)
            )
            
            ListItem(
                headlineContent = { Text("Public Profile", fontWeight = FontWeight.SemiBold) },
                supportingContent = { Text("Allow anyone to see your profile photo and name.") },
                leadingContent = { 
                    Icon(
                        Icons.Default.AccountCircle, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                trailingContent = {
                    Switch(
                        checked = isPublicProfile,
                        onCheckedChange = { 
                            isPublicProfile = it
                            updateSetting("public_profile", it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                },
                modifier = Modifier.clickable { 
                    isPublicProfile = !isPublicProfile 
                    updateSetting("public_profile", isPublicProfile)
                }
            )
            
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
            
            ListItem(
                headlineContent = { Text("Birthday Notifications", fontWeight = FontWeight.SemiBold) },
                supportingContent = { Text("Notify your friends when it's your birthday.") },
                leadingContent = { 
                    Icon(
                        Icons.Default.Notifications, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                trailingContent = {
                    Switch(
                        checked = isBirthdayNotificationEnabled,
                        onCheckedChange = { 
                            isBirthdayNotificationEnabled = it
                            updateSetting("birthday_notifications", it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                },
                modifier = Modifier.clickable { 
                    isBirthdayNotificationEnabled = !isBirthdayNotificationEnabled 
                    updateSetting("birthday_notifications", isBirthdayNotificationEnabled)
                }
            )
            
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
            
            ListItem(
                headlineContent = { Text("Live Location Sharing", fontWeight = FontWeight.SemiBold) },
                supportingContent = { Text("Allow close friends to see your location in real-time.") },
                leadingContent = { 
                    Icon(
                        Icons.Default.ShareLocation, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                trailingContent = {
                    Switch(
                        checked = isLiveLocationShared,
                        onCheckedChange = { 
                            isLiveLocationShared = it 
                            updateSetting("live_location", it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                },
                modifier = Modifier.clickable { 
                    isLiveLocationShared = !isLiveLocationShared 
                    updateSetting("live_location", isLiveLocationShared)
                }
            )
            
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
            
            ListItem(
                headlineContent = { Text("Chat Visibility", fontWeight = FontWeight.SemiBold) },
                supportingContent = { Text("Show when you are active or typing in conversations.") },
                leadingContent = { 
                    Icon(
                        Icons.Default.Visibility, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                trailingContent = {
                    Switch(
                        checked = isChatVisible,
                        onCheckedChange = { 
                            isChatVisible = it 
                            updateSetting("chat_visible", it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                },
                modifier = Modifier.clickable { 
                    isChatVisible = !isChatVisible 
                    updateSetting("chat_visible", isChatVisible)
                }
            )
            
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
            
            Box {
                ListItem(
                    headlineContent = { Text("Status Updates Privacy", fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("Who can view your status updates: $statusPrivacy") },
                    leadingContent = { 
                        Icon(
                            Icons.Default.Group, 
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        ) 
                    },
                    modifier = Modifier.clickable { 
                        isStatusPrivacyMenuExpanded = true
                    }
                )
                
                DropdownMenu(
                    expanded = isStatusPrivacyMenuExpanded,
                    onDismissRequest = { isStatusPrivacyMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Everyone") },
                        onClick = {
                            statusPrivacy = "Everyone"
                            sharedPrefs.edit().putString("status_privacy", "Everyone").apply()
                            isStatusPrivacyMenuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Contacts") },
                        onClick = {
                            statusPrivacy = "Contacts"
                            sharedPrefs.edit().putString("status_privacy", "Contacts").apply()
                            isStatusPrivacyMenuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Selected Friends") },
                        onClick = {
                            statusPrivacy = "Selected Friends"
                            sharedPrefs.edit().putString("status_privacy", "Selected Friends").apply()
                            isStatusPrivacyMenuExpanded = false
                        }
                    )
                }
            }
            
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
            
            ListItem(
                headlineContent = { Text("Private Chat Mode", fontWeight = FontWeight.SemiBold) },
                supportingContent = { Text("Enable end-to-end encryption for all new direct messages.") },
                leadingContent = { 
                    Icon(
                        Icons.Default.Lock, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                trailingContent = {
                    Switch(
                        checked = isPrivateChatEnabled,
                        onCheckedChange = { 
                            isPrivateChatEnabled = it 
                            updateSetting("private_chat", it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                },
                modifier = Modifier.clickable { 
                    isPrivateChatEnabled = !isPrivateChatEnabled 
                    updateSetting("private_chat", isPrivateChatEnabled)
                }
            )
        }
    }
}
