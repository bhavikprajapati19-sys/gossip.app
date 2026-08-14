package com.example.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("app_settings", Context.MODE_PRIVATE) }

    fun updateSetting(key: String, value: Boolean) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
        ) {
            
            // --- NOTIFICATIONS ---
            SettingsSectionHeader(title = "Notifications", icon = Icons.Default.Notifications)
            
            var pushNotifications by remember { mutableStateOf(sharedPrefs.getBoolean("push_notifications", true)) }
            SettingsSwitchItem(
                title = "Push Notifications",
                subtitle = "Receive push notifications for messages and updates.",
                checked = pushNotifications,
                onCheckedChange = { pushNotifications = it; updateSetting("push_notifications", it) }
            )

            var messageNotifications by remember { mutableStateOf(sharedPrefs.getBoolean("message_notifications", true)) }
            SettingsSwitchItem(
                title = "Message Notifications",
                subtitle = "Get notified when you receive a new message.",
                checked = messageNotifications,
                onCheckedChange = { messageNotifications = it; updateSetting("message_notifications", it) }
            )
            
            var likeCommentNotifications by remember { mutableStateOf(sharedPrefs.getBoolean("like_comment", true)) }
            SettingsSwitchItem(
                title = "Like & Comment Notifications",
                subtitle = "Get notified when someone interacts with your posts.",
                checked = likeCommentNotifications,
                onCheckedChange = { likeCommentNotifications = it; updateSetting("like_comment", it) }
            )

            // --- PRIVACY ---
            SettingsSectionHeader(title = "Privacy", icon = Icons.Default.Lock)
            
            var privateAccount by remember { mutableStateOf(sharedPrefs.getBoolean("private_account", false)) }
            SettingsSwitchItem(
                title = "Private Account",
                subtitle = "Only approved followers can see your posts and stories.",
                checked = privateAccount,
                onCheckedChange = { privateAccount = it; updateSetting("private_account", it) }
            )
            
            var onlineStatus by remember { mutableStateOf(sharedPrefs.getBoolean("online_status", true)) }
            SettingsSwitchItem(
                title = "Show Online Status",
                subtitle = "Let others see when you are active.",
                checked = onlineStatus,
                onCheckedChange = { onlineStatus = it; updateSetting("online_status", it) }
            )
            
            var readReceipts by remember { mutableStateOf(sharedPrefs.getBoolean("read_receipts", true)) }
            SettingsSwitchItem(
                title = "Read Receipts",
                subtitle = "Let others know when you have read their messages.",
                checked = readReceipts,
                onCheckedChange = { readReceipts = it; updateSetting("read_receipts", it) }
            )
            
            // --- CONTENT & DATA ---
            SettingsSectionHeader(title = "Content & Data", icon = Icons.Default.DataUsage)
            
            var dataSaver by remember { mutableStateOf(sharedPrefs.getBoolean("data_saver", false)) }
            SettingsSwitchItem(
                title = "Data Saver",
                subtitle = "Reduce data usage by loading lower quality media.",
                checked = dataSaver,
                onCheckedChange = { dataSaver = it; updateSetting("data_saver", it) }
            )
            
            var highQualityUploads by remember { mutableStateOf(sharedPrefs.getBoolean("hq_uploads", true)) }
            SettingsSwitchItem(
                title = "High-Quality Media Uploads",
                subtitle = "Always upload photos and videos in highest quality.",
                checked = highQualityUploads,
                onCheckedChange = { highQualityUploads = it; updateSetting("hq_uploads", it) }
            )

            // --- APPEARANCE ---
            SettingsSectionHeader(title = "Appearance", icon = Icons.Default.Palette)
            
            var darkMode by remember { mutableStateOf(sharedPrefs.getBoolean("dark_mode", false)) }
            SettingsSwitchItem(
                title = "Dark Mode",
                subtitle = "Use a darker theme for the app interface.",
                checked = darkMode,
                onCheckedChange = { darkMode = it; updateSetting("dark_mode", it) }
            )

            // --- SECURITY ---
            SettingsSectionHeader(title = "Security", icon = Icons.Default.Security)
            
            var twoFactorAuth by remember { mutableStateOf(sharedPrefs.getBoolean("two_factor", false)) }
            SettingsSwitchItem(
                title = "Two-Factor Authentication",
                subtitle = "Require an extra step to log in.",
                checked = twoFactorAuth,
                onCheckedChange = { twoFactorAuth = it; updateSetting("two_factor", it) }
            )
            
            var appLock by remember { mutableStateOf(sharedPrefs.getBoolean("app_lock", false)) }
            SettingsSwitchItem(
                title = "App Lock / Face ID",
                subtitle = "Require biometric authentication to open the app.",
                checked = appLock,
                onCheckedChange = { appLock = it; updateSetting("app_lock", it) }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(subtitle) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        modifier = Modifier.clickable { onCheckedChange(!checked) }
    )
}
