content = open('app/src/main/java/com/example/navigation/AppNavigation.kt').read()

# Add import
if 'import com.example.screens.FriendProfileScreen' not in content:
    content = content.replace('import com.example.screens.ProfileScreen', 'import com.example.screens.ProfileScreen\nimport com.example.screens.FriendProfileScreen')

# Modify ChatScreen navigation
if 'onNavigateToCall =' in content and 'onNavigateToFriendProfile' not in content:
    content = content.replace(
        '                onNavigateToCall = { isVideo ->\n                    navController.navigate("call/$chatId/$isVideo")\n                }',
        '''                onNavigateToCall = { isVideo ->
                    navController.navigate("call/$chatId/$isVideo")
                },
                onNavigateToFriendProfile = {
                    navController.navigate("friend_profile/$chatId")
                }'''
    )

# Add FriendProfileScreen composable
if 'friend_profile' not in content:
    content = content.replace(
        '        composable("profile") {',
        '''        composable("friend_profile/{chatId}") { backStackEntry ->
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

        composable("profile") {'''
    )

open('app/src/main/java/com/example/navigation/AppNavigation.kt', 'w').write(content)
