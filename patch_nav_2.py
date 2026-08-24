content = open('app/src/main/java/com/example/navigation/AppNavigation.kt').read()

if 'friend_profile/{chatId}' not in content:
    content = content.replace(
        '''        composable("profile") {''',
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
