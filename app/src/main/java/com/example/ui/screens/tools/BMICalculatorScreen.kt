package com.example.ui.screens.tools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.catalog.ToolCatalog
import com.example.data.preferences.AppLanguage
import com.example.ui.components.NexoraTopBar
import com.example.ui.components.ResultCard
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.VibrantCyan
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel
import java.text.DecimalFormat

@Composable
fun BMICalculatorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "calc_bmi" } }
    val tool = remember { ToolCatalog.getToolById("calc_bmi")!! }
    val context = LocalContext.current

    var weightKgStr by remember { mutableStateOf("68") }
    var heightCmStr by remember { mutableStateOf("175") }

    val w = weightKgStr.toDoubleOrNull() ?: 0.0
    val hCm = heightCmStr.toDoubleOrNull() ?: 0.0

    val (bmiVal, category, healthyRange) = remember(w, hCm) {
        if (w > 0 && hCm > 0) {
            val hM = hCm / 100.0
            val bmi = w / (hM * hM)
            val cat = when {
                bmi < 18.5 -> "Underweight"
                bmi < 24.9 -> "Normal weight (Healthy)"
                bmi < 29.9 -> "Overweight"
                else -> "Obesity"
            }
            val minW = 18.5 * hM * hM
            val maxW = 24.9 * hM * hM
            val fmt = DecimalFormat("#.#")
            Triple(bmi, cat, "Healthy Weight Range: ${fmt.format(minW)} kg – ${fmt.format(maxW)} kg")
        } else Triple(0.0, "", "")
    }

    val bmiFmt = DecimalFormat("#.#").format(bmiVal)

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
                value = weightKgStr,
                onValueChange = { weightKgStr = it },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_bmi_weight")
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = heightCmStr,
                onValueChange = { heightCmStr = it },
                label = { Text("Height (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_bmi_height")
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (bmiVal > 0) {
                ResultCard(
                    title = "BMI Category: $category",
                    resultText = "$bmiFmt kg/m²",
                    onCopy = { PlatformUtils.copyToClipboard(context, "BMI: $bmiFmt ($category) - $healthyRange") },
                    onShare = { PlatformUtils.shareText(context, "BMI: $bmiFmt ($category) - $healthyRange") }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(healthyRange, fontWeight = FontWeight.SemiBold, color = VibrantCyan)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Disclaimer: BMI is a general screening indicator and does not directly measure body fat or replace medical evaluation.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
