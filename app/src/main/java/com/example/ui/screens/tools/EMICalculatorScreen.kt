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
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel
import java.text.DecimalFormat
import kotlin.math.pow

@Composable
fun EMICalculatorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "calc_emi" } }
    val tool = remember { ToolCatalog.getToolById("calc_emi")!! }
    val context = LocalContext.current

    var principalStr by remember { mutableStateOf("1000000") }
    var interestRateStr by remember { mutableStateOf("8.5") }
    var tenureYearsStr by remember { mutableStateOf("15") }

    val p = principalStr.toDoubleOrNull() ?: 0.0
    val annualR = interestRateStr.toDoubleOrNull() ?: 0.0
    val years = tenureYearsStr.toDoubleOrNull() ?: 0.0

    val (monthlyEmi, totalInterest, totalPayable) = remember(p, annualR, years) {
        if (p > 0 && annualR > 0 && years > 0) {
            val r = (annualR / 12.0) / 100.0
            val n = years * 12.0
            val emi = (p * r * (1.0 + r).pow(n)) / ((1.0 + r).pow(n) - 1.0)
            val total = emi * n
            val interest = total - p
            Triple(emi, interest, total)
        } else {
            Triple(0.0, 0.0, 0.0)
        }
    }

    val fmt = DecimalFormat("#,##0")

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
                value = principalStr,
                onValueChange = { principalStr = it },
                label = { Text("Loan Principal Amount (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_loan_principal")
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = interestRateStr,
                onValueChange = { interestRateStr = it },
                label = { Text("Annual Interest Rate (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_interest_rate")
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = tenureYearsStr,
                onValueChange = { tenureYearsStr = it },
                label = { Text("Tenure (Years)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_tenure_years")
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (monthlyEmi > 0) {
                ResultCard(
                    title = "Monthly Loan EMI",
                    resultText = "₹${fmt.format(monthlyEmi)} / mo",
                    onCopy = {
                        val text = "Loan EMI: ₹${fmt.format(monthlyEmi)}/mo | Total Interest: ₹${fmt.format(totalInterest)} | Total Amount: ₹${fmt.format(totalPayable)}"
                        PlatformUtils.copyToClipboard(context, text)
                    },
                    onShare = {
                        val text = "Loan EMI: ₹${fmt.format(monthlyEmi)}/mo | Total Interest: ₹${fmt.format(totalInterest)} | Total Amount: ₹${fmt.format(totalPayable)}"
                        PlatformUtils.shareText(context, text)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Principal Amount:", style = MaterialTheme.typography.bodyMedium)
                            Text("₹${fmt.format(p)}", fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Interest Payable:", style = MaterialTheme.typography.bodyMedium)
                            Text("₹${fmt.format(totalInterest)}", fontWeight = FontWeight.Bold, color = ElectricPurple)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Amount Payable:", style = MaterialTheme.typography.bodyMedium)
                            Text("₹${fmt.format(totalPayable)}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
