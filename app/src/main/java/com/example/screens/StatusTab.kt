package com.example.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import java.util.UUID

data class StatusViewer(
    val name: String,
    val avatarUrl: String? = null,
    val time: String,
    val reaction: String? = null
)

data class StatusUpdate(
    val id: String,
    val authorName: String,
    val timeAgo: String,
    val avatarUrl: String? = null,
    val mediaUri: Uri? = null,
    val isViewed: Boolean = false,
    val isMine: Boolean = false,
    val views: List<StatusViewer> = emptyList(),
    val caption: String = "",
    val taggedFriends: List<String> = emptyList()
)

val initialMockStatuses = listOf(
    StatusUpdate(
        id = "s1",
        authorName = "Sarah Miller",
        timeAgo = "10 minutes ago",
        avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah",
        caption = "Beautiful day! ☀️",
        taggedFriends = listOf("Alex Rivera", "My Name")
    ),
    StatusUpdate(
        id = "s2",
        authorName = "Marcus Chen",
        timeAgo = "2 hours ago",
        avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=Marcus",
        isViewed = true
    )
)

val mockViewers = listOf(
    StatusViewer("Alex Rivera", "https://api.dicebear.com/7.x/avataaars/svg?seed=Alex", "5 mins ago", "❤️"),
    StatusViewer("Jordan Lee", "https://api.dicebear.com/7.x/avataaars/svg?seed=Jordan", "10 mins ago", "🔥"),
    StatusViewer("Taylor Swift", "https://api.dicebear.com/7.x/avataaars/svg?seed=Taylor", "1 hr ago", "😂")
)

@Composable
fun StatusViewDialog(status: StatusUpdate, onDismiss: () -> Unit, onReact: (String) -> Unit, onReshare: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth().height(400.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = status.avatarUrl ?: status.mediaUri,
                    contentDescription = "Status Content",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Overlay info
                Column(
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopStart)
                        .background(androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent)
                        )).padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White)) {
                            AsyncImage(model = status.avatarUrl, contentDescription = null, modifier = Modifier.fillMaxSize())
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(status.authorName, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(status.timeAgo, color = Color.White.copy(alpha=0.8f), fontSize = 12.sp)
                        }
                        if (status.taggedFriends.contains("My Name") && !status.isMine) {
                            IconButton(onClick = onReshare, modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)) {
                                Icon(Icons.Default.Share, "Reshare", tint = Color.White)
                            }
                        }
                    }
                    if (status.caption.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(status.caption, color = Color.White)
                    }
                    if (status.taggedFriends.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("With: ${status.taggedFriends.joinToString(", ")}", color = Color.White.copy(alpha=0.8f), fontSize = 12.sp)
                    }
                }
                
                // Reaction Bar
                if (!status.isMine) {
                    Row(
                        modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).fillMaxWidth().background(Color.Black.copy(alpha=0.5f), RoundedCornerShape(24.dp)).padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("❤️", "😂", "😮", "😢", "🔥").forEach { emoji ->
                            Text(
                                text = emoji, 
                                fontSize = 24.sp, 
                                modifier = Modifier.clickable { onReact(emoji) }.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusViewsDialog(myStatuses: List<StatusUpdate>, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("Viewed by", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(mockViewers) { viewer ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = viewer.avatarUrl,
                                contentDescription = viewer.name,
                                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(viewer.name, fontWeight = FontWeight.SemiBold)
                                Text(viewer.time, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (viewer.reaction != null) {
                                Text(viewer.reaction, fontSize = 20.sp)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun StatusTab() {
    val context = LocalContext.current
    var myStatuses by remember { mutableStateOf(listOf<StatusUpdate>()) }
    val mockStatuses by remember { mutableStateOf(initialMockStatuses) }
    var viewingStatus by remember { mutableStateOf<StatusUpdate?>(null) }
    var viewingMyStatusViews by remember { mutableStateOf(false) }
    var pendingStatusUri by remember { mutableStateOf<Uri?>(null) }
    
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            pendingStatusUri = uri
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Create Status",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatusActionButton(
                        icon = Icons.Default.CameraAlt,
                        label = "Photo & Music",
                        onClick = { 
                            Toast.makeText(context, "Opening Camera with Music & Layout options...", Toast.LENGTH_SHORT).show()
                            mediaPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                        }
                    )
                    
                    StatusActionButton(
                        icon = Icons.Default.Edit,
                        label = "Text",
                        onClick = { Toast.makeText(context, "Opening Text Status Editor...", Toast.LENGTH_SHORT).show() }
                    )
                    
                    StatusActionButton(
                        icon = Icons.Default.Mic,
                        label = "Voice",
                        onClick = { Toast.makeText(context, "Hold to Record Voice Status...", Toast.LENGTH_SHORT).show() }
                    )
                }
            }
        }
        
        item {
            MyStatusItem(
                myStatuses = myStatuses,
                onAddClick = {
                    mediaPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                },
                onViewClick = {
                    if (myStatuses.isNotEmpty()) {
                        viewingMyStatusViews = true
                    } else {
                        mediaPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                    }
                }
            )
        }
        
        item {
            Text(
                text = "Recent Updates",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }
        
        items(mockStatuses) { status ->
            StatusRow(
                status = status,
                onClick = { viewingStatus = status }
            )
        }
    }

    if (viewingStatus != null) {
        StatusViewDialog(
            status = viewingStatus!!,
            onDismiss = { viewingStatus = null },
            onReact = { reaction ->
                Toast.makeText(context, "Sent $reaction to ${viewingStatus!!.authorName}", Toast.LENGTH_SHORT).show()
                viewingStatus = null
            },
            onReshare = {
                Toast.makeText(context, "Reshared status to your updates!", Toast.LENGTH_SHORT).show()
                val resharedStatus = StatusUpdate(
                    id = UUID.randomUUID().toString(),
                    authorName = "My Status",
                    timeAgo = "Just now",
                    mediaUri = viewingStatus!!.mediaUri,
                    avatarUrl = viewingStatus!!.avatarUrl, // keep original media
                    isMine = true,
                    caption = "Reshared: ${viewingStatus!!.caption}"
                )
                myStatuses = listOf(resharedStatus) + myStatuses
                viewingStatus = null
            }
        )
    }
    
    if (viewingMyStatusViews) {
        StatusViewsDialog(
            myStatuses = myStatuses,
            onDismiss = { viewingMyStatusViews = false }
        )
    }

    if (pendingStatusUri != null) {
        var captionText by remember { mutableStateOf("") }
        var tagsText by remember { mutableStateOf("") }
        
        Dialog(onDismissRequest = { pendingStatusUri = null }) {
            Surface(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Create Status", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    AsyncImage(model = pendingStatusUri, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = captionText, onValueChange = { captionText = it }, label = { Text("Caption (Optional)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = tagsText, onValueChange = { tagsText = it }, label = { Text("Tag Friends (e.g. Alex, Jordan)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { pendingStatusUri = null }) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            val taggedList = tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            val newStatus = StatusUpdate(
                                id = UUID.randomUUID().toString(),
                                authorName = "My Status",
                                timeAgo = "Just now",
                                mediaUri = pendingStatusUri,
                                isMine = true,
                                views = mockViewers,
                                caption = captionText,
                                taggedFriends = taggedList
                            )
                            myStatuses = listOf(newStatus) + myStatuses
                            pendingStatusUri = null
                        }) {
                            Text("Post")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyStatusItem(myStatuses: List<StatusUpdate>, onAddClick: () -> Unit, onViewClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onViewClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(56.dp)) {
            val mostRecent = myStatuses.firstOrNull()
            if (mostRecent?.mediaUri != null) {
                AsyncImage(
                    model = mostRecent.mediaUri,
                    contentDescription = "My Status",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("JD", color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                }
            }
            
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                    .clickable(onClick = onAddClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(14.dp))
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "My Status",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (myStatuses.isNotEmpty()) "${myStatuses.size} updates" else "Tap to add status update",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        if (myStatuses.isNotEmpty()) {
            val totalViews = myStatuses.sumOf { it.views.size }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Visibility, contentDescription = "Views", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(4.dp))
                Text("$totalViews", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun StatusRow(status: StatusUpdate, onClick: () -> Unit) {
    val borderColor = if (status.isViewed) MaterialTheme.colorScheme.outline.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primary
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .border(2.dp, borderColor, CircleShape)
                .padding(2.dp)
        ) {
            AsyncImage(
                model = status.avatarUrl ?: status.mediaUri,
                contentDescription = status.authorName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = status.authorName,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = status.timeAgo,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatusActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
