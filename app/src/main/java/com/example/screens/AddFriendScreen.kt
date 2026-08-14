package com.example.screens

import android.content.Context
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ContactInfo(val name: String, val number: String)

fun fetchContacts(context: Context): List<ContactInfo> {
    val contacts = mutableListOf<ContactInfo>()
    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null, null, null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )
    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        while (it.moveToNext()) {
            val name = it.getString(nameIndex) ?: ""
            val number = it.getString(numberIndex) ?: ""
            if (name.isNotEmpty() && number.isNotEmpty()) {
                contacts.add(ContactInfo(name, number))
            }
        }
    }
    return contacts.distinctBy { it.number.replace(Regex("[^0-9+]"), "") }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(onNavigateBack: () -> Unit) {
    var countryCode by remember { mutableStateOf("+1") }
    var showCountryPicker by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    
    var showContactsSheet by remember { mutableStateOf(false) }
    var contactsList by remember { mutableStateOf<List<ContactInfo>>(emptyList()) }
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            coroutineScope.launch {
                contactsList = fetchContacts(context)
                showContactsSheet = true
            }
        } else {
            resultMessage = "Permission denied to read contacts."
        }
    }

    val isPhoneValid = phoneNumber.length >= 10

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Friend") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PersonAdd,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Connect with Mobile Number",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Enter your friend's phone number to find them or invite them to the app.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = countryCode,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier.weight(0.3f).clickable { showCountryPicker = true },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
                
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it.filter { char -> char.isDigit() } },
                    label = { Text("Phone Number") },
                    modifier = Modifier.weight(0.7f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    shape = RoundedCornerShape(16.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { 
                    coroutineScope.launch {
                        isLoading = true
                        resultMessage = null
                        delay(1500)
                        isLoading = false
                        resultMessage = "Invitation sent to $countryCode $phoneNumber!"
                        phoneNumber = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                enabled = isPhoneValid && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Add Friend",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (resultMessage != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = resultMessage!!,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Text("OR", color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedButton(
                onClick = { permissionLauncher.launch(android.Manifest.permission.READ_CONTACTS) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.Contacts, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text(
                    text = "Import from Contacts",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showContactsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showContactsSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Your Contacts",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    if (contactsList.isEmpty()) {
                        Text(
                            text = "No contacts found.",
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn {
                            items(contactsList) { contact ->
                                ListItem(
                                    headlineContent = { Text(contact.name) },
                                    supportingContent = { Text(contact.number) },
                                    modifier = Modifier.clickable {
                                        showContactsSheet = false
                                        coroutineScope.launch {
                                            isLoading = true
                                            resultMessage = null
                                            delay(1500)
                                            isLoading = false
                                            resultMessage = "Invitation sent to ${contact.name} (${contact.number})!"
                                        }
                                    }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
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
