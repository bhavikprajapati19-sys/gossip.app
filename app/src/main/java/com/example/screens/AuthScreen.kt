package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Phone
import com.example.R

// Royal Colors
val RoyalGold = Color(0xFFD4AF37)
val RoyalPurple = Color(0xFF2C043D)
val RoyalNavy = Color(0xFF0F172A)
val RoyalSurface = Color(0x99000000)

@Composable
fun AuthScreen(onSendOtp: (String) -> Unit) {
    var countryCode by remember { mutableStateOf("+1") }
    var showCountryPicker by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    val isPhoneValid = phoneNumber.length >= 10

    Scaffold(
        containerColor = Color.Transparent
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
                // Royal Logo Placeholder
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .border(2.dp, RoyalGold, RoundedCornerShape(32.dp))
                        .background(RoyalSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "♔",
                        fontSize = 64.sp,
                        color = RoyalGold
                    )
                }
                
                Text(
                    text = "GOSSIP",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = RoyalGold,
                    letterSpacing = 4.sp,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Exclusive Conversations.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 48.dp)
                )
                
                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = RoyalGold.copy(alpha = 0.5f),
                    focusedBorderColor = RoyalGold,
                    unfocusedContainerColor = RoyalSurface,
                    focusedContainerColor = RoyalSurface.copy(alpha = 0.8f),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    focusedLabelColor = RoyalGold,
                    cursorColor = RoyalGold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = countryCode,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .weight(0.3f)
                            .clickable { showCountryPicker = true },
                        enabled = false,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(8.dp),
                        colors = textFieldColors.copy(
                            disabledContainerColor = RoyalSurface,
                            disabledTextColor = Color.White,
                            disabledIndicatorColor = RoyalGold.copy(alpha = 0.5f)
                        ),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, color = Color.White)
                    )
                    
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it.filter { char -> char.isDigit() } },
                        label = { Text("Phone Number", color = Color.LightGray) },
                        modifier = Modifier.weight(0.7f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = RoyalGold) },
                        shape = RoundedCornerShape(8.dp),
                        colors = textFieldColors
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = acceptedTerms,
                        onCheckedChange = { acceptedTerms = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = RoyalGold,
                            checkmarkColor = Color.Black,
                            uncheckedColor = RoyalGold.copy(alpha = 0.5f)
                        )
                    )
                    Text(
                        text = "I agree to the ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                    Text(
                        text = "Royal Decrees",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RoyalGold,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { showTermsDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { onSendOtp("$countryCode$phoneNumber") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, if (isPhoneValid && acceptedTerms) RoyalGold else RoyalGold.copy(alpha=0.3f), RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    enabled = isPhoneValid && acceptedTerms,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RoyalGold.copy(alpha = 0.15f),
                        contentColor = RoyalGold,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = RoyalGold.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "ENTER COURT",
                        fontSize = 16.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
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
                
            if (showTermsDialog) {
                AlertDialog(
                    onDismissRequest = { showTermsDialog = false },
                    title = { Text("Royal Decrees (Terms)", color = RoyalGold, fontFamily = FontFamily.Serif) },
                    text = {
                        androidx.compose.foundation.lazy.LazyColumn {
                            item {
                                Text(
                                    "Welcome to Gossip!\n\n" +
                                    "By using our app, you agree to these terms. Please read them carefully.\n\n" +
                                    "1. User Conduct\n" +
                                    "You agree not to use the app to post or share any abusive, hateful, or illegal content. We reserve the right to ban accounts that violate these rules.\n\n" +
                                    "2. Privacy\n" +
                                    "Your messages are processed for real-time communication. While we strive to protect your data, please be mindful of what you share.\n\n" +
                                    "3. Age Requirements\n" +
                                    "You must be at least 13 years old to use this app. By accepting these terms, you confirm that you meet this age requirement.\n\n" +
                                    "4. Intellectual Property\n" +
                                    "You retain ownership of the content you share, but grant us a license to host and display it within the service.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.LightGray
                                )
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { 
                            acceptedTerms = true
                            showTermsDialog = false 
                        }) {
                            Text("Accept", color = RoyalGold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTermsDialog = false }) {
                            Text("Close", color = Color.Gray)
                        }
                    },
                    containerColor = RoyalNavy,
                    titleContentColor = RoyalGold,
                    textContentColor = Color.White
                )
            }
        }
    }
}
