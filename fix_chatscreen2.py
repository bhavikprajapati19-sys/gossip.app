lines = open('app/src/main/java/com/example/screens/ChatScreen.kt').read()

# Let's completely rewrite the file with the correct contents
with open('app/src/main/java/com/example/screens/ChatScreen.kt', 'w') as f:
    f.write('''package com.example.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import coil.compose.AsyncImage
import java.util.UUID

enum class AttachmentType { IMAGE, VIDEO, DOCUMENT, AUDIO, LOCATION, CONTACT }

data class Message(
    val id: String, 
    val text: String, 
    val isMine: Boolean, 
    val time: String,
    val attachmentUri: String? = null,
    val attachmentType: AttachmentType? = null,
    val reactions: List<String> = emptyList()
)

val mockMessages = listOf(
    Message("1", "Hey, how are you?", false, "10:30 AM"),
    Message("2", "I'm doing well! How about you?", true, "10:32 AM", reactions = listOf("❤️", "🔥")),
    Message("3", "Just working on that new project.", false, "10:35 AM"),
    Message("4", "Oh nice, need any help?", true, "10:40 AM", reactions = listOf("👍")),
    Message("5", "Hey, are we still on for tomorrow?", false, "10:42 AM"),
    Message("6", "Voice Note (0:15)", false, "10:45 AM", attachmentType = AttachmentType.AUDIO)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String, 
    onNavigateBack: () -> Unit,
    onNavigateToCall: (Boolean) -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf(*mockMessages.toTypedArray()) }
    var showAttachmentMenu by remember { mutableStateOf(false) }
    var isOtherPartyTyping by remember { mutableStateOf(false) }
    
    LaunchedEffect(messages.size) {
        if (messages.lastOrNull()?.isMine == true) {
            isOtherPartyTyping = true
            kotlinx.coroutines.delay(3000)
            isOtherPartyTyping = false
            messages.add(
                Message(
                    id = UUID.randomUUID().toString(),
                    text = "Sounds good! 👍",
                    isMine = false,
                    time = "Just now"
                )
            )
        }
    }
    
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            messages.add(
                Message(
                    id = UUID.randomUUID().toString(),
                    text = "",
                    isMine = true,
                    time = "Just now",
                    attachmentUri = it.toString(),
                    attachmentType = AttachmentType.IMAGE
                )
            )
        }
    }
    
    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            messages.add(
                Message(
                    id = UUID.randomUUID().toString(),
                    text = "Document attached",
                    isMine = true,
                    time = "Just now",
                    attachmentUri = it.toString(),
                    attachmentType = AttachmentType.DOCUMENT
                )
            )
        }
    }
    
    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            messages.add(Message(UUID.randomUUID().toString(), "", true, "Just now", attachmentType = AttachmentType.AUDIO))
        }
    }
    
    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->
        uri?.let {
            messages.add(
                Message(
                    id = UUID.randomUUID().toString(),
                    text = "Shared Contact",
                    isMine = true,
                    time = "Just now",
                    attachmentUri = it.toString(),
                    attachmentType = AttachmentType.CONTACT
                )
            )
        }
    }
    
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            messages.add(
                Message(
                    id = UUID.randomUUID().toString(),
                    text = "https://maps.google.com/?q=current+location",
                    isMine = true,
                    time = "Just now",
                    attachmentType = AttachmentType.LOCATION
                )
            )
        }
    }
    
    val historicBackgrounds = listOf(
        com.example.R.drawable.img_historic_india_1_1787147522050,
        com.example.R.drawable.img_historic_india_2_1787147543595,
        com.example.R.drawable.img_historic_india_3_1787147566220
    )
    val randomBackground = remember { historicBackgrounds.random() }

    val chatName = mockChats.find { it.id == chatId }?.name ?: "Unknown"
    var showOptionsMenu by remember { mutableStateOf(false) }
    val isBlocked = blockedUserIds.contains(chatId)

    Box(modifier = Modifier.fillMaxSize()) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = randomBackground),
            contentDescription = "Historic Indian Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f)))

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                    ),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(chatName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                if (isOtherPartyTyping) {
                                    Text(
                                        text = "typing...",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { onNavigateToCall(false) }) {
                            Icon(Icons.Default.Call, contentDescription = "Voice Call")
                        }
                        IconButton(onClick = { onNavigateToCall(true) }) {
                            Icon(Icons.Default.Videocam, contentDescription = "Video Call")
                        }
                        Box {
                            IconButton(onClick = { showOptionsMenu = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More options")
                            }
                            DropdownMenu(
                                expanded = showOptionsMenu,
                                onDismissRequest = { showOptionsMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(if (isBlocked) "Unblock User" else "Block User") },
                                    onClick = {
                                        showOptionsMenu = false
                                        if (isBlocked) {
                                            blockedUserIds.remove(chatId)
                                        } else {
                                            blockedUserIds.add(chatId)
                                        }
                                    }
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (isBlocked) {
                    BottomAppBar(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "You blocked this contact.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    BottomAppBar(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                    ) {
                        Box {
                            IconButton(onClick = { showAttachmentMenu = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Media")
                            }
                            DropdownMenu(
                                expanded = showAttachmentMenu,
                                onDismissRequest = { showAttachmentMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Photo or Video") },
                                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null) },
                                    onClick = {
                                        showAttachmentMenu = false
                                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Audio") },
                                    leadingIcon = { Icon(Icons.Default.Mic, contentDescription = null) },
                                    onClick = {
                                        showAttachmentMenu = false
                                        micPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Location") },
                                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                                    onClick = {
                                        showAttachmentMenu = false
                                        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Contact") },
                                    leadingIcon = { Icon(Icons.Default.Contacts, contentDescription = null) },
                                    onClick = {
                                        showAttachmentMenu = false
                                        contactPickerLauncher.launch(null)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Document") },
                                    leadingIcon = { Icon(Icons.Default.InsertDriveFile, contentDescription = null) },
                                    onClick = {
                                        showAttachmentMenu = false
                                        documentPickerLauncher.launch(arrayOf("*/*"))
                                    }
                                )
                            }
                        }
                        
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            placeholder = { Text("Message") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            shape = RoundedCornerShape(24.dp)
                        )
                        
                        IconButton(
                            onClick = {
                                if (messageText.isNotBlank()) {
                                    messages.add(
                                        Message(
                                            id = UUID.randomUUID().toString(),
                                            text = messageText.trim(),
                                            isMine = true,
                                            time = "Just now"
                                        )
                                    )
                                    messageText = ""
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                        }
                    }
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                reverseLayout = true
            ) {
                if (isOtherPartyTyping) {
                    item {
                        TypingIndicator()
                    }
                }
                
                items(messages.reversed(), key = { it.id }) { message ->
                    MessageBubble(
                        message = message,
                        onReact = { emoji ->
                            val index = messages.indexOfFirst { it.id == message.id }
                            if (index != -1) {
                                val currentReactions = messages[index].reactions.toMutableList()
                                if (currentReactions.contains(emoji)) {
                                    currentReactions.remove(emoji)
                                } else {
                                    currentReactions.add(emoji)
                                }
                                messages[index] = messages[index].copy(reactions = currentReactions)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, onReact: (String) -> Unit = {}) {
    var showReactionMenu by remember { mutableStateOf(false) }
    
    val alignment = if (message.isMine) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (message.isMine) MaterialTheme.colorScheme.primary.copy(alpha = 0.9f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
    val contentColor = if (message.isMine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val shape = if (message.isMine) {
        RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Column(horizontalAlignment = if (message.isMine) Alignment.End else Alignment.Start) {
            Box {
                DropdownMenu(
                    expanded = showReactionMenu,
                    onDismissRequest = { showReactionMenu = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        listOf("❤️", "😂", "😮", "😢", "🔥", "👍").forEach { emoji ->
                            Text(
                                text = emoji,
                                fontSize = 24.sp,
                                modifier = Modifier
                                    .clickable {
                                        onReact(emoji)
                                        showReactionMenu = false
                                    }
                                    .padding(4.dp)
                            )
                        }
                    }
                }
                
                Box(
                    modifier = Modifier
                        .clip(shape)
                        .background(backgroundColor)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    onReact("❤️")
                                },
                                onLongPress = {
                                    showReactionMenu = true
                                }
                            )
                        }
                        .padding(if (message.attachmentType == AttachmentType.IMAGE && message.text.isEmpty()) 4.dp else 12.dp)
                ) {
                    Column {
                        if (message.attachmentType != null) {
                            when (message.attachmentType) {
                                AttachmentType.IMAGE, AttachmentType.VIDEO -> {
                                    AsyncImage(
                                        model = message.attachmentUri,
                                        contentDescription = "Attachment",
                                        modifier = Modifier
                                            .fillMaxWidth(0.7f)
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    if (message.text.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                                AttachmentType.DOCUMENT -> {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                            .padding(8.dp)
                                            .fillMaxWidth(0.7f)
                                    ) {
                                        Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = contentColor)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Document", color = contentColor, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    if (message.text.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                                AttachmentType.AUDIO -> {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                                            .padding(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(contentColor.copy(alpha = 0.2f))
                                                .clickable { /* Play audio */ },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Mic, contentDescription = null, tint = contentColor)
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                        AudioWaveform(modifier = Modifier.weight(1f, fill = false))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("0:15", color = contentColor, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(end = 8.dp))
                                    }
                                    if (message.text.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                                AttachmentType.LOCATION -> {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                            .padding(12.dp)
                                            .fillMaxWidth(0.7f)
                                    ) {
                                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = contentColor)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Shared Location", color = contentColor, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    if (message.text.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                                AttachmentType.CONTACT -> {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                            .padding(12.dp)
                                            .fillMaxWidth(0.7f)
                                    ) {
                                        Icon(Icons.Default.Contacts, contentDescription = null, tint = contentColor)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Shared Contact", color = contentColor, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    if (message.text.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                                else -> {}
                            }
                        }
                        if (message.text.isNotEmpty()) {
                            Text(
                                text = message.text,
                                color = contentColor,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
                
                if (message.reactions.isNotEmpty()) {
                    ReactionRow(reactions = message.reactions, isMine = message.isMine)
                }
            }
            
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = message.time,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AudioWaveform(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(24.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        val heights = listOf(0.4f, 0.7f, 1.0f, 0.5f, 0.3f, 0.8f, 0.6f, 0.9f, 0.4f, 0.2f, 0.5f, 0.7f)
        heights.forEach { fraction ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight(fraction)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f))
            )
        }
    }
}

@Composable
fun ReactionRow(reactions: List<String>, isMine: Boolean) {
    if (reactions.isEmpty()) return
    Row(
        modifier = Modifier
            .padding(top = 2.dp)
            .offset(x = if (isMine) (-8).dp else 8.dp, y = (-8).dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        reactions.forEach { emoji ->
            Text(text = emoji, fontSize = 12.sp)
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("Typing", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}
''')
