import re

with open('app/src/main/java/com/example/screens/ChatScreen.kt', 'r') as f:
    content = f.read()

# Fix TopAppBar closing paren (around line 255)
content = content.replace('''                }
            )
        },
        bottomBar = {''', '''                }
                )
            },
            bottomBar = {''')

# Fix the end of MessageBubble
content = content.replace('''        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = message.time,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } // closes Column
} // closes Top-level Box
} // closes MessageBubble function''', '''        }
        
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = message.time,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}''')

# Fix Scaffold bracket closures in ChatScreen
# At line 389-391
content = content.replace('''        }
    }
}

@Composable
fun MessageBubble''', '''        }
    }
}

@Composable
fun MessageBubble''')

# Let's just fix specific line issues using Python
