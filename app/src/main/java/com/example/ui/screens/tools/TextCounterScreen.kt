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
import androidx.compose.ui.unit.sp
import com.example.data.catalog.ToolCatalog
import com.example.data.preferences.AppLanguage
import com.example.ui.components.ActionButton
import com.example.ui.components.NexoraTopBar
import com.example.ui.components.ResultCard
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.VibrantCyan
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel

@Composable
fun TextCounterScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "text_counter" } }
    val tool = remember { ToolCatalog.getToolById("text_counter")!! }
    val context = LocalContext.current

    var textInput by remember { mutableStateOf("") }

    data class TextStats(
        val words: String,
        val charsNoSpace: String,
        val charsWithSpace: String,
        val sentences: String,
        val paragraphs: String,
        val readingTimeMins: String
    )

    val stats = remember(textInput) {
        val trimmed = textInput.trim()
        val w = if (trimmed.isEmpty()) 0 else trimmed.split("\\s+".toRegex()).size
        val cNoSpace = textInput.count { !it.isWhitespace() }
        val cSpace = textInput.length
        val s = if (trimmed.isEmpty()) 0 else trimmed.split("[.!?]+".toRegex()).filter { it.isNotBlank() }.size
        val p = if (trimmed.isEmpty()) 0 else textInput.split("\n+".toRegex()).filter { it.isNotBlank() }.size
        val rTime = String.format("%.1f", w / 200.0)
        TextStats(w.toString(), cNoSpace.toString(), cSpace.toString(), s.toString(), p.toString(), "$rTime min")
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
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text("Paste or type text to analyze...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .testTag("input_text_counter"),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ActionButton(
                    text = "Clear Text",
                    onClick = { textInput = "" },
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Copy Stats",
                    onClick = {
                        val statsStr = "Words: ${stats.words} | Characters: ${stats.charsWithSpace} | Sentences: ${stats.sentences} | Paragraphs: ${stats.paragraphs} | Est. Reading Time: ${stats.readingTimeMins}"
                        PlatformUtils.copyToClipboard(context, statsStr)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Grid Cards
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().border(1.dp, ElectricPurple.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        StatItem("Words", stats.words)
                        StatItem("Characters", stats.charsWithSpace)
                        StatItem("No Spaces", stats.charsNoSpace)
                    }
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        StatItem("Sentences", stats.sentences)
                        StatItem("Paragraphs", stats.paragraphs)
                        StatItem("Reading Time", stats.readingTimeMins)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = VibrantCyan)
    }
}
