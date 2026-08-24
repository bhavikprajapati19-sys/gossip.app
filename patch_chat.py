content = open('app/src/main/java/com/example/screens/ChatScreen.kt').read()

if 'onNavigateToFriendProfile: () -> Unit' not in content:
    content = content.replace(
        '''    chatId: String, 
    onNavigateBack: () -> Unit,
    onNavigateToCall: (Boolean) -> Unit
) {''',
        '''    chatId: String, 
    onNavigateBack: () -> Unit,
    onNavigateToCall: (Boolean) -> Unit,
    onNavigateToFriendProfile: () -> Unit = {}
) {'''
    )

if '.background(MaterialTheme.colorScheme.primaryContainer),' in content and '.clickable { onNavigateToFriendProfile() }' not in content:
    content = content.replace(
        '''                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                            }''',
        '''                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .clickable { onNavigateToFriendProfile() },
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = mockChats.find { it.id == chatId }?.avatarUrl,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }'''
    )

open('app/src/main/java/com/example/screens/ChatScreen.kt', 'w').write(content)
