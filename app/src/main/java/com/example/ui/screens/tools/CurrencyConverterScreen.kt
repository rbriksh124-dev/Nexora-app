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
fun CurrencyConverterScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "conv_currency" } }
    val tool = remember { ToolCatalog.getToolById("conv_currency")!! }
    val context = LocalContext.current

    val currencies = listOf(
        "USD" to 1.0,
        "INR" to 84.50,
        "EUR" to 0.92,
        "GBP" to 0.79,
        "JPY" to 154.20,
        "CAD" to 1.39,
        "AUD" to 1.51,
        "AED" to 3.67,
        "SGD" to 1.33
    )

    var amountStr by remember { mutableStateOf("100") }
    var fromIndex by remember { mutableStateOf(0) } // USD
    var toIndex by remember { mutableStateOf(1) } // INR
    var customRateStr by remember { mutableStateOf("") }

    val fromCurr = currencies[fromIndex]
    val toCurr = currencies[toIndex]

    val effectiveRate = remember(fromCurr, toCurr, customRateStr) {
        val custom = customRateStr.toDoubleOrNull()
        if (custom != null && custom > 0) custom
        else (toCurr.second / fromCurr.second)
    }

    val convertedAmount = remember(amountStr, effectiveRate) {
        val amt = amountStr.toDoubleOrNull()
        if (amt != null) {
            val res = amt * effectiveRate
            val fmt = DecimalFormat("#,##0.00").format(res)
            "$amt ${fromCurr.first} = $fmt ${toCurr.first}"
        } else ""
    }

    Scaffold(
        topBar = {
            NexoraTopBar(
                title = if (language == AppLanguage.HINDI) tool.nameHi else tool.nameEn,
                subtitle = "Manual & Standard Forex Rates (Offline Ready)",
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
                value = amountStr,
                onValueChange = { amountStr = it },
                label = { Text("Amount to Convert") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_curr_amt")
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("From Currency", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                currencies.take(4).forEachIndexed { idx, pair ->
                    FilterChip(
                        selected = fromIndex == idx,
                        onClick = { fromIndex = idx },
                        label = { Text(pair.first) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("To Currency", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                currencies.take(4).forEachIndexed { idx, pair ->
                    FilterChip(
                        selected = toIndex == idx,
                        onClick = { toIndex = idx },
                        label = { Text(pair.first) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = customRateStr,
                onValueChange = { customRateStr = it },
                label = { Text("Manual Custom Rate (Optional Override)") },
                placeholder = { Text("e.g. 84.50") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_custom_rate")
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (convertedAmount.isNotEmpty()) {
                ResultCard(
                    title = "Exchange Summary (@ 1 ${fromCurr.first} = ${DecimalFormat("#.####").format(effectiveRate)} ${toCurr.first})",
                    resultText = convertedAmount,
                    onCopy = { PlatformUtils.copyToClipboard(context, convertedAmount) },
                    onShare = { PlatformUtils.shareText(context, convertedAmount) }
                )
            }
        }
    }
}
