package com.example.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    Country("MX", "+52", "Mexico", "🇲🇽"),
    Country("IT", "+39", "Italy", "🇮🇹"),
    Country("ES", "+34", "Spain", "🇪🇸"),
    Country("RU", "+7", "Russia", "🇷🇺"),
    Country("CN", "+86", "China", "🇨🇳"),
    Country("AE", "+971", "United Arab Emirates", "🇦🇪"),
    Country("AR", "+54", "Argentina", "🇦🇷"),
    Country("BD", "+880", "Bangladesh", "🇧🇩"),
    Country("CO", "+57", "Colombia", "🇨🇴"),
    Country("EG", "+20", "Egypt", "🇪🇬"),
    Country("ID", "+62", "Indonesia", "🇮🇩"),
    Country("IR", "+98", "Iran", "🇮🇷"),
    Country("IQ", "+964", "Iraq", "🇮🇶"),
    Country("KE", "+254", "Kenya", "🇰🇪"),
    Country("KR", "+82", "South Korea", "🇰🇷"),
    Country("MY", "+60", "Malaysia", "🇲🇾"),
    Country("NG", "+234", "Nigeria", "🇳🇬"),
    Country("PH", "+63", "Philippines", "🇵🇭"),
    Country("PK", "+92", "Pakistan", "🇵🇰"),
    Country("SA", "+966", "Saudi Arabia", "🇸🇦"),
    Country("TH", "+66", "Thailand", "🇹🇭"),
    Country("TR", "+90", "Turkey", "🇹🇷"),
    Country("VN", "+84", "Vietnam", "🇻🇳")
    // Note: Kept to a solid top 30 list to cover major demographics.
).sortedBy { it.name }

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
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search country or code...") },
                singleLine = true
            )
            
            LazyColumn {
                items(filteredCountries) { country ->
                    ListItem(
                        headlineContent = { Text(country.name) },
                        leadingContent = { Text(country.flag) },
                        trailingContent = { Text(country.dialCode) },
                        modifier = Modifier.clickable {
                            onCountrySelected(country)
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
