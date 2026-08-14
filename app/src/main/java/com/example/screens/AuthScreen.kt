package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Phone
import androidx.compose.foundation.clickable

@Composable
fun AuthScreen(onSendOtp: (String) -> Unit) {
    var countryCode by remember { mutableStateOf("+1") }
    var showCountryPicker by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }

    val isPhoneValid = phoneNumber.length >= 10

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
        containerColor = Color(0xFF09090B)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Gradient Orbs Background
            Box(modifier = Modifier.fillMaxSize().background(neonPurpleGlow))
            Box(modifier = Modifier.fillMaxSize().background(neonCyanGlow))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.ic_gossip_logo),
                    contentDescription = "Gossip Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(32.dp))
                )
                
                Text(
                    text = "Gossip",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = (-2).sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Enter your phone number to start.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 48.dp)
                )
                
                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = countryCode,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.weight(0.3f).clickable { showCountryPicker = true },
                        enabled = false,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors.copy(
                            disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledIndicatorColor = MaterialTheme.colorScheme.outline
                        ),
                        textStyle = LocalTextStyle.current.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    )
                    
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it.filter { char -> char.isDigit() } },
                        label = { Text("Phone Number") },
                        modifier = Modifier.weight(0.7f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors
                    )
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Button(
                    onClick = { onSendOtp("$countryCode$phoneNumber") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    enabled = isPhoneValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Send Code",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            if (showCountryPicker) {
                CountryPickerBottomSheet(
                    onDismissRequest = { showCountryPicker = false },
                    onCountrySelected = { 
                        countryCode = it.dialCode
                        showCountryPicker = false
                    }
                )
            }
        }
    }
}
