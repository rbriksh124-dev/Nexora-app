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
import java.text.DecimalFormat

@Composable
fun PercentageCalculatorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "calc_percentage" } }
    val tool = remember { ToolCatalog.getToolById("calc_percentage")!! }
    val context = LocalContext.current

    var percentVal by remember { mutableStateOf("15") }
    var totalVal by remember { mutableStateOf("250") }
    var resultText by remember { mutableStateOf("") }

    fun calculate() {
        val p = percentVal.toDoubleOrNull()
        val t = totalVal.toDoubleOrNull()
        if (p != null && t != null) {
            val ans = (p / 100.0) * t
            val formatted = DecimalFormat("#,##0.##").format(ans)
            resultText = "$p% of $t = $formatted"
            viewModel.logHistory(tool, "$p% of $t", formatted)
        }
    }

    LaunchedEffect(percentVal, totalVal) {
        calculate()
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
            Text(
                text = if (language == AppLanguage.HINDI) "मूल्य दर्ज करें" else "Input Percentage & Total",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = percentVal,
                onValueChange = { percentVal = it },
                label = { Text(if (language == AppLanguage.HINDI) "प्रतिशत (%)" else "Percentage (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_percentage")
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = totalVal,
                onValueChange = { totalVal = it },
                label = { Text(if (language == AppLanguage.HINDI) "कुल मान / आधार" else "Total / Base Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_total")
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (resultText.isNotEmpty()) {
                ResultCard(
                    title = if (language == AppLanguage.HINDI) "गणना परिणाम" else "Calculated Result",
                    resultText = resultText,
                    onCopy = { PlatformUtils.copyToClipboard(context, resultText) },
                    onShare = { PlatformUtils.shareText(context, resultText) }
                )
            }
        }
    }
}

@Composable
fun DiscountCalculatorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "calc_discount" } }
    val tool = remember { ToolCatalog.getToolById("calc_discount")!! }
    val context = LocalContext.current

    var originalPrice by remember { mutableStateOf("1200") }
    var discountPercent by remember { mutableStateOf("25") }
    var resultText by remember { mutableStateOf("") }
    var savingsText by remember { mutableStateOf("") }

    fun calculate() {
        val price = originalPrice.toDoubleOrNull()
        val disc = discountPercent.toDoubleOrNull()
        if (price != null && disc != null) {
            val savings = (disc / 100.0) * price
            val finalPrice = price - savings
            val fmtFinal = DecimalFormat("#,##0.00").format(finalPrice)
            val fmtSave = DecimalFormat("#,##0.00").format(savings)
            resultText = "Final: ₹$fmtFinal"
            savingsText = "You Save: ₹$fmtSave ($disc%)"
            viewModel.logHistory(tool, "Original ₹$price, $disc% off", "Final ₹$fmtFinal (Saved ₹$fmtSave)")
        }
    }

    LaunchedEffect(originalPrice, discountPercent) {
        calculate()
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
                value = originalPrice,
                onValueChange = { originalPrice = it },
                label = { Text(if (language == AppLanguage.HINDI) "मूल मूल्य (₹)" else "Original Price (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_orig_price")
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = discountPercent,
                onValueChange = { discountPercent = it },
                label = { Text(if (language == AppLanguage.HINDI) "छूट प्रतिशत (%)" else "Discount Percentage (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_discount_percent")
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (resultText.isNotEmpty()) {
                ResultCard(
                    title = savingsText,
                    resultText = resultText,
                    onCopy = { PlatformUtils.copyToClipboard(context, "$resultText, $savingsText") },
                    onShare = { PlatformUtils.shareText(context, "$resultText, $savingsText") }
                )
            }
        }
    }
}
