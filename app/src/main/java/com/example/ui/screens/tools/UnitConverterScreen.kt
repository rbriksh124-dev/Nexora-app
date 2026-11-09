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
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel
import java.text.DecimalFormat

@Composable
fun UnitConverterScreen(
    viewModel: MainViewModel,
    toolId: String,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == toolId } }
    val tool = remember { ToolCatalog.getToolById(toolId) ?: ToolCatalog.tools.first() }
    val context = LocalContext.current

    val unitOptions = remember(toolId) {
        when (toolId) {
            "conv_length" -> listOf("Meter" to 1.0, "Kilometer" to 1000.0, "Centimeter" to 0.01, "Millimeter" to 0.001, "Inch" to 0.0254, "Foot" to 0.3048, "Yard" to 0.9144, "Mile" to 1609.344)
            "conv_weight" -> listOf("Kilogram" to 1.0, "Gram" to 0.001, "Milligram" to 0.000001, "Pound (lb)" to 0.45359237, "Ounce (oz)" to 0.0283495, "Metric Ton" to 1000.0)
            "conv_digital" -> listOf("Bytes" to 1.0, "Kilobytes (KB)" to 1024.0, "Megabytes (MB)" to 1048576.0, "Gigabytes (GB)" to 1073741824.0, "Terabytes (TB)" to 1099511627776.0)
            "conv_speed" -> listOf("km/h" to 1.0, "m/s" to 3.6, "mph" to 1.60934, "knots" to 1.852)
            "conv_area" -> listOf("Sq Meters" to 1.0, "Sq Kilometers" to 1000000.0, "Sq Feet" to 0.092903, "Acre" to 4046.86, "Hectare" to 10000.0)
            "conv_volume" -> listOf("Liter" to 1.0, "Milliliter" to 0.001, "Cubic Meter" to 1000.0, "US Gallon" to 3.78541, "Cup" to 0.24)
            "conv_pressure" -> listOf("Pascal" to 1.0, "Bar" to 100000.0, "PSI" to 6894.76, "Atmosphere (atm)" to 101325.0)
            "conv_power" -> listOf("Watt" to 1.0, "Kilowatt" to 1000.0, "Horsepower (hp)" to 745.7)
            else -> listOf("Base Unit" to 1.0, "Kilo" to 1000.0, "Milli" to 0.001)
        }
    }

    var inputVal by remember { mutableStateOf("10") }
    var fromIndex by remember { mutableStateOf(0) }
    var toIndex by remember { mutableStateOf(if (unitOptions.size > 1) 1 else 0) }

    val fromUnit = unitOptions.getOrElse(fromIndex) { unitOptions.first() }
    val toUnit = unitOptions.getOrElse(toIndex) { unitOptions.last() }

    val result = remember(inputVal, fromIndex, toIndex) {
        val num = inputVal.toDoubleOrNull()
        if (num != null) {
            val baseVal = num * fromUnit.second
            val converted = baseVal / toUnit.second
            val fmt = DecimalFormat("#,##0.######").format(converted)
            "$num ${fromUnit.first} = $fmt ${toUnit.first}"
        } else ""
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
                value = inputVal,
                onValueChange = { inputVal = it },
                label = { Text("Value to Convert") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_unit_val")
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("From Unit", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                unitOptions.take(4).forEachIndexed { idx, pair ->
                    FilterChip(
                        selected = fromIndex == idx,
                        onClick = { fromIndex = idx },
                        label = { Text(pair.first.split(" ").first()) },
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("To Unit", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                unitOptions.take(4).forEachIndexed { idx, pair ->
                    FilterChip(
                        selected = toIndex == idx,
                        onClick = { toIndex = idx },
                        label = { Text(pair.first.split(" ").first()) },
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (result.isNotEmpty()) {
                ResultCard(
                    title = "Conversion Result",
                    resultText = result,
                    onCopy = { PlatformUtils.copyToClipboard(context, result) },
                    onShare = { PlatformUtils.shareText(context, result) }
                )
            }
        }
    }
}
