package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.screens.AuthScreen
import com.example.screens.ChatListScreen
import com.example.screens.ChatScreen
import com.example.screens.OtpScreen
import com.example.screens.ProfileScreen
import com.example.screens.SettingsScreen

import com.example.screens.AddFriendScreen
import com.example.screens.CallScreen
import com.example.screens.FriendsListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {
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
        
        composable("profile") {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
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
