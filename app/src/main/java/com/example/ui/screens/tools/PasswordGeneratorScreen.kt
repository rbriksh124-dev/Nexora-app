package com.example.ui.screens.tools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.catalog.ToolCatalog
import com.example.data.preferences.AppLanguage
import com.example.ui.components.ActionButton
import com.example.ui.components.NexoraTopBar
import com.example.ui.components.ResultCard
import com.example.ui.theme.ElectricPurple
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel
import java.security.SecureRandom

@Composable
fun PasswordGeneratorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "text_password" } }
    val tool = remember { ToolCatalog.getToolById("text_password")!! }
    val context = LocalContext.current

    var passwordLength by remember { mutableStateOf(16f) }
    var includeUppercase by remember { mutableStateOf(true) }
    var includeLowercase by remember { mutableStateOf(true) }
    var includeNumbers by remember { mutableStateOf(true) }
    var includeSymbols by remember { mutableStateOf(true) }

    var generatedPassword by remember { mutableStateOf("") }

    fun generate() {
        val uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
        val numberChars = "0123456789"
        val symbolChars = "!@#$%^&*()_+-=[]{}|;:,.<>?"

        val pool = StringBuilder()
        if (includeUppercase) pool.append(uppercaseChars)
        if (includeLowercase) pool.append(lowercaseChars)
        if (includeNumbers) pool.append(numberChars)
        if (includeSymbols) pool.append(symbolChars)

        if (pool.isNotEmpty()) {
            val random = SecureRandom()
            val len = passwordLength.toInt()
            val sb = StringBuilder()
            for (i in 0 until len) {
                sb.append(pool[random.nextInt(pool.length)])
            }
            generatedPassword = sb.toString()
            viewModel.logHistory(tool, "Length $len", generatedPassword.take(4) + "••••••••")
        }
    }

    LaunchedEffect(Unit) {
        generate()
    }

    val strengthRating = remember(passwordLength, includeSymbols, includeNumbers, includeUppercase) {
        val len = passwordLength.toInt()
        when {
            len >= 16 && includeSymbols && includeNumbers && includeUppercase -> "Very Strong (Bank Grade)"
            len >= 12 && includeNumbers -> "Strong"
            len >= 8 -> "Moderate"
            else -> "Weak"
        }
    }

    Scaffold(
        topBar = {
            NexoraTopBar(
                title = if (language == AppLanguage.HINDI) tool.nameHi else tool.nameEn,
                subtitle = if (language == AppLanguage.HINDI) tool.descriptionHi else tool.descriptionEn,
                onBackClick = onBack,
                isFavorite = isFav,
                onFavoriteToggle = { viewModel.toggleFavorite(tool, isFav) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            if (generatedPassword.isNotEmpty()) {
                ResultCard(
                    title = "Strength: $strengthRating",
                    resultText = generatedPassword,
                    onCopy = { PlatformUtils.copyToClipboard(context, generatedPassword) },
                    onShare = { PlatformUtils.shareText(context, generatedPassword) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ActionButton(
                text = "Regenerate Password",
                onClick = { generate() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Password Length: ${passwordLength.toInt()}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Slider(
                value = passwordLength,
                onValueChange = {
                    passwordLength = it
                    generate()
                },
                valueRange = 8f..32f,
                steps = 23,
                modifier = Modifier.testTag("slider_pwd_length")
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    CheckboxRow("Include Uppercase (A-Z)", includeUppercase) { includeUppercase = it; generate() }
                    CheckboxRow("Include Lowercase (a-z)", includeLowercase) { includeLowercase = it; generate() }
                    CheckboxRow("Include Numbers (0-9)", includeNumbers) { includeNumbers = it; generate() }
                    CheckboxRow("Include Symbols (!@#$)", includeSymbols) { includeSymbols = it; generate() }
                }
            }
        }
    }
}

@Composable
private fun CheckboxRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
    }
}
