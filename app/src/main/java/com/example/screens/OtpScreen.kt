package com.example.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.R

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

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = RoyalGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Royal Background Image
            Image(
                painter = painterResource(id = R.drawable.img_royal_bg_1787146791490),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Semi-transparent overlay to ensure text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                    ))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "VERIFICATION",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = RoyalGold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                    
                Text(
                    text = "Enter the royal cipher sent to",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray,
                    fontFamily = FontFamily.Serif,
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
                                            color = if (isFocused) RoyalGold else RoyalGold.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(
                                            RoyalSurface,
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = char,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = RoyalGold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                )
                   
                Spacer(modifier = Modifier.height(32.dp))
                   
                Text(
                    text = if (timer > 0) "Resend scroll in \${timer}s" else "Resend scroll",
                    color = if (timer > 0) Color.Gray else RoyalGold,
                    fontFamily = FontFamily.Serif,
                    fontWeight = if (timer > 0) FontWeight.Normal else FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                   
                Button(
                    onClick = onVerifySuccess,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, if (otp.length == 6) RoyalGold else RoyalGold.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    enabled = otp.length == 6,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RoyalGold.copy(alpha = 0.15f),
                        contentColor = RoyalGold,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = RoyalGold.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "CONFIRM",
                        fontSize = 16.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        }
    }
}
