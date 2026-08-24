#!/bin/bash
cat << 'INNER_EOF' > replacement.txt
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { isSearchActive = true }) {
                                Text("🔍", fontSize = 24.sp)
                            }
                            IconButton(onClick = onNavigateToCreateGroup) {
                                Icon(Icons.Default.GroupAdd, contentDescription = "Create Group")
                            }
                        }
                    }
                }
                   
                Spacer(modifier = Modifier.height(16.dp))
                   
                if (selectedBottomTab == 0) {
                    androidx.compose.foundation.lazy.LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf("All", "Unread", "Favourites", "Groups")
                        items(filters.size) { index ->
                            val filter = filters[index]
                            NavPill(
                                text = filter, 
                                isSelected = chatFilter == filter, 
                                onClick = { chatFilter = filter }
                            )
                        }
                    }
                }
            }
        },
INNER_EOF

# Extract part before Row(horizontalArrangement...
head -n 185 app/src/main/java/com/example/screens/ChatListScreen.kt > temp_chat.kt
cat replacement.txt >> temp_chat.kt
# Extract part after NavPill block
tail -n +239 app/src/main/java/com/example/screens/ChatListScreen.kt >> temp_chat.kt
mv temp_chat.kt app/src/main/java/com/example/screens/ChatListScreen.kt
