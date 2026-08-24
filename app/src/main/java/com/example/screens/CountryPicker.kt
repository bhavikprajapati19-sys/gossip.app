package com.example.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

data class Country(val code: String, val dialCode: String, val name: String, val flag: String)

val ALL_COUNTRIES = listOf(
    Country("US", "+1", "United States", "🇺🇸"),
    Country("GB", "+44", "United Kingdom", "🇬🇧"),
    Country("IN", "+91", "India", "🇮🇳"),
    Country("CA", "+1", "Canada", "🇨🇦"),
    Country("AU", "+61", "Australia", "🇦🇺"),
    Country("DE", "+49", "Germany", "🇩🇪"),
    Country("FR", "+33", "France", "🇫🇷"),
    Country("JP", "+81", "Japan", "🇯🇵"),
    Country("BR", "+55", "Brazil", "🇧🇷"),
    Country("ZA", "+27", "South Africa", "🇿🇦"),
    Country("MX", "+52", "Mexico", "🇲🇽")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPickerBottomSheet(
    onDismissRequest: () -> Unit,
    onCountrySelected: (Country) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCountries = ALL_COUNTRIES.filter { 
        it.name.contains(searchQuery, ignoreCase = true) || it.dialCode.contains(searchQuery)
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = RoyalNavy,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Text(
                text = "Select Kingdom",
                style = MaterialTheme.typography.titleLarge,
                color = RoyalGold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search kingdom or scroll...", color = Color.LightGray) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = RoyalGold.copy(alpha = 0.5f),
                    focusedBorderColor = RoyalGold,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    cursorColor = RoyalGold
                )
            )

            LazyColumn {
                items(filteredCountries) { country ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCountrySelected(country) }
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        Text(text = country.flag, modifier = Modifier.padding(end = 16.dp))
                        Text(text = country.name, modifier = Modifier.weight(1f), color = Color.White)
                        Text(text = country.dialCode, color = RoyalGold)
                    }
                }
            }
        }
    }
}
