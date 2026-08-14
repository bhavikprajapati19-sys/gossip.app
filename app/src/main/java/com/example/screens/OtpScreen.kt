package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(phoneNumber: String, onVerifySuccess: () -> Unit, onNavigateBack: () -> Unit) {
    var otp by remember { mutableStateOf("") }
    var timer by remember { mutableIntStateOf(60) }
    
    LaunchedEffect(timer) {
        if (timer > 0) {
            delay(1000)
            timer -= 1
        }
    }

    val neonPurpleGlow = Brush.radialGradient(
        colors = listOf(Color(0xFFC084FC).copy(alpha = 0.35f), Color.Transparent),
        center = Offset(0f, 0f),
        radius = 1200f
    )
    val neonCyanGlow = Brush.radialGradient(
        colors = listOf(Color(0xFF22D3EE).copy(alpha = 0.25f), Color.Transparent),
        center = Offset(1000f, 2000f),
        radius = 1500f
    )

    Scaffold(
        containerColor = Color(0xFF09090B),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(modifier = Modifier.fillMaxSize().background(neonPurpleGlow))
            Box(modifier = Modifier.fillMaxSize().background(neonCyanGlow))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Verification",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = (-2).sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Enter the 6-digit code sent to",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = phoneNumber,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // OTP Input
                BasicTextField(
                    value = otp,
                    onValueChange = { if (it.length <= 6) otp = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    decorationBox = {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            repeat(6) { index ->
                                val char = when {
                                    index >= otp.length -> ""
                                    else -> otp[index].toString()
                                }
                                val isFocused = index == otp.length
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .border(
                                            width = if (isFocused) 2.dp else 1.dp,
                                            color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .background(
                                            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                            RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = char,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = if (timer > 0) "Resend code in ${timer}s" else "Resend code",
                    color = if (timer > 0) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                    fontWeight = if (timer > 0) FontWeight.Normal else FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                Button(
                    onClick = onVerifySuccess,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    enabled = otp.length == 6,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Verify & Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
