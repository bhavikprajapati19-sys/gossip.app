package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.screens.AuthScreen
import com.example.screens.ChatBackupManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.example.screens.ChatListScreen
import com.example.screens.ChatScreen
import com.example.screens.OtpScreen
import com.example.screens.ProfileScreen
import com.example.screens.FriendProfileScreen
import com.example.screens.SettingsScreen

import com.example.screens.AddFriendScreen
import com.example.screens.CallScreen
import com.example.screens.FriendsListScreen
import com.example.screens.CreateGroupScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE) }
    val startDestination = remember { if (sharedPrefs.getBoolean("is_logged_in", false)) "chat_list" else "auth" }

    LaunchedEffect(Unit) {
        val backupManager = ChatBackupManager(context)
        while (isActive) {
            // Check if auto-backup is enabled
            if (sharedPrefs.getBoolean("auto_backup", false)) {
                backupManager.exportChatHistoryToLocal()
            }
            // Run every 15 minutes (or 10 seconds for testing - using 15 mins for realism)
            delay(15 * 60 * 1000L)
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("auth") {
            AuthScreen(
                onSendOtp = { phone ->
                    navController.navigate("otp/$phone")
                }
            )
        }
        
        composable("otp/{phone}") { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpScreen(
                phoneNumber = phone,
                onVerifySuccess = {
                    sharedPrefs.edit().putBoolean("is_logged_in", true).apply()
                    navController.navigate("chat_list") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("chat_list") {
            ChatListScreen(
                onNavigateToChat = { chatId ->
                    navController.navigate("chat/$chatId")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToAddFriend = {
                    navController.navigate("add_friend")
                },
                onNavigateToFriends = {
                    navController.navigate("friends_list")
                },
                onNavigateToCreateGroup = {
                    navController.navigate("create_group")
                }
            )
        }
        
        composable("create_group") {
            CreateGroupScreen(
                onNavigateBack = { navController.popBackStack() },
                onGroupCreated = { groupId ->
                    navController.navigate("chat/$groupId") {
                        popUpTo("chat_list")
                    }
                }
            )
        }
        
        composable("chat/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatScreen(
                chatId = chatId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCall = { isVideo ->
                    navController.navigate("call/$chatId/$isVideo")
                },
                onNavigateToFriendProfile = {
                    navController.navigate("friend_profile/$chatId")
                }
            )
        }
        
        composable("call/{chatId}/{isVideo}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            val isVideo = backStackEntry.arguments?.getString("isVideo")?.toBoolean() ?: false
            CallScreen(
                chatId = chatId,
                isVideoCall = isVideo,
                onEndCall = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("friend_profile/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            FriendProfileScreen(
                chatId = chatId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCall = { isVideo ->
                    navController.navigate("call/$chatId/$isVideo")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    sharedPrefs.edit().putBoolean("is_logged_in", false).apply()
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("add_friend") {
            AddFriendScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("friends_list") {
            FriendsListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToChat = { chatId ->
                    navController.navigate("chat/$chatId")
                },
                onNavigateToAddFriend = {
                    navController.navigate("add_friend")
                }
            )
        }
    }
}
