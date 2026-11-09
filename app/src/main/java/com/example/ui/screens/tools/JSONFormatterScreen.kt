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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.catalog.ToolCatalog
import com.example.data.preferences.AppLanguage
import com.example.ui.components.ActionButton
import com.example.ui.components.NexoraTopBar
import com.example.ui.components.ResultCard
import com.example.ui.theme.ElectricPurple
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun JSONFormatterScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "dev_json" } }
    val tool = remember { ToolCatalog.getToolById("dev_json")!! }
    val context = LocalContext.current

    var jsonInput by remember { mutableStateOf("{\"name\":\"Nexora\",\"version\":1,\"features\":[\"calculators\",\"converters\",\"dev\"]}") }
    var formattedOutput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun prettify() {
        val str = jsonInput.trim()
        if (str.isEmpty()) {
            formattedOutput = ""
            errorMessage = null
            return
        }
        try {
            if (str.startsWith("[")) {
                val array = JSONArray(str)
                formattedOutput = array.toString(2)
            } else {
                val obj = JSONObject(str)
                formattedOutput = obj.toString(2)
            }
            errorMessage = null
            viewModel.logHistory(tool, "Formatted JSON (${str.length} chars)", "Valid JSON")
        } catch (e: Exception) {
            errorMessage = "Invalid JSON: ${e.message}"
        }
    }

    fun minify() {
        val str = jsonInput.trim()
        try {
            if (str.startsWith("[")) {
                val array = JSONArray(str)
                formattedOutput = array.toString()
            } else {
                val obj = JSONObject(str)
                formattedOutput = obj.toString()
            }
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = "Invalid JSON: ${e.message}"
        }
    }

    LaunchedEffect(Unit) {
        prettify()
    }

    Scaffold(
        topBar = {
            NexoraTopBar(
                title = if (language == AppLanguage.HINDI) tool.nameHi else tool.nameEn,
                subtitle = "Prettify, Minify & Validate JSON Offline",
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
            OutlinedTextField(
                value = jsonInput,
                onValueChange = { jsonInput = it },
                label = { Text("Paste raw JSON string...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .testTag("input_raw_json"),
                shape = RoundedCornerShape(16.dp),
                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace, fontSize = 13.sp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ActionButton(
                    text = "Prettify (2 Spaces)",
                    onClick = { prettify() },
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Minify (Compact)",
                    onClick = { minify() },
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            if (formattedOutput.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Formatted Output", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    TextButton(onClick = { PlatformUtils.copyToClipboard(context, formattedOutput) }) {
                        Text("Copy Formatted JSON")
                    }
                }
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().border(1.dp, ElectricPurple.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = formattedOutput,
                        modifier = Modifier.padding(16.dp),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
