package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun CallScreen(
    chatId: String,
    isVideoCall: Boolean,
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isVideoEnabled by remember { mutableStateOf(isVideoCall) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isVideoEnabled) Color.DarkGray else MaterialTheme.colorScheme.surface)
    ) {
        // Mock full-screen video background if video is enabled
        if (isVideoEnabled) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isVideoEnabled) {
                    AsyncImage(
                        model = "https://api.dicebear.com/7.x/avataaars/svg?seed=$chatId",
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                } else {
                    // Small floating video thumbnail for self
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(100.dp, 150.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color.DarkGray)
                            .align(Alignment.End)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = if (chatId.isBlank()) "Unknown Contact" else chatId.replaceFirstChar { it.uppercase() },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isVideoEnabled) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Ringing...",
                    fontSize = 16.sp,
                    color = if (isVideoEnabled) Color.LightGray else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Call Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mute Toggle
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = if (isVideoEnabled) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
                
                // Video Toggle
                IconButton(
                    onClick = { isVideoEnabled = !isVideoEnabled },
                    modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isVideoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Video",
                        tint = if (isVideoEnabled) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }

                // End Call
                IconButton(
                    onClick = onEndCall,
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.Red, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
