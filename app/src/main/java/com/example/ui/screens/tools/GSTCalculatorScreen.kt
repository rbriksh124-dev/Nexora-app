package com.example.ui.screens.tools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun GSTCalculatorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "calc_gst" } }
    val tool = remember { ToolCatalog.getToolById("calc_gst")!! }
    val context = LocalContext.current

    var amountStr by remember { mutableStateOf("1000") }
    var selectedGSTRate by remember { mutableStateOf(18.0) }
    var isExclusive by remember { mutableStateOf(true) } // Exclusive vs Inclusive
    var isInterState by remember { mutableStateOf(false) } // IGST vs CGST/SGST

    val gstRates = listOf(5.0, 12.0, 18.0, 28.0)

    val amount = amountStr.toDoubleOrNull() ?: 0.0
    val (netAmount, gstAmount, totalAmount) = remember(amount, selectedGSTRate, isExclusive) {
        if (isExclusive) {
            val gst = (amount * selectedGSTRate) / 100.0
            Triple(amount, gst, amount + gst)
        } else {
            val net = (amount * 100.0) / (100.0 + selectedGSTRate)
            val gst = amount - net
            Triple(net, gst, amount)
        }
    }

    val fmt = DecimalFormat("#,##0.00")

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
            // Mode selector (Exclusive vs Inclusive)
            Row(modifier = Modifier.fillMaxWidth()) {
                FilterChip(
                    selected = isExclusive,
                    onClick = { isExclusive = true },
                    label = { Text("GST Exclusive (+ Add)") },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )
                FilterChip(
                    selected = !isExclusive,
                    onClick = { isExclusive = false },
                    label = { Text("GST Inclusive (- Remove)") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amountStr,
                onValueChange = { amountStr = it },
                label = { Text(if (isExclusive) "Base Price Amount (₹)" else "Total Inclusive Amount (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_gst_amount")
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Select GST Slab Rate (%)",
                style = MaterialTheme.typography.labelLarge.copy(color = ElectricPurple, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                gstRates.forEach { rate ->
                    FilterChip(
                        selected = selectedGSTRate == rate,
                        onClick = { selectedGSTRate = rate },
                        label = { Text("${rate.toInt()}%") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Transaction Type", style = MaterialTheme.typography.bodyMedium)
                Row {
                    FilterChip(
                        selected = !isInterState,
                        onClick = { isInterState = false },
                        label = { Text("Intra-State (CGST+SGST)") },
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    FilterChip(
                        selected = isInterState,
                        onClick = { isInterState = true },
                        label = { Text("Inter-State (IGST)") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Breakdown Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().border(1.dp, ElectricPurple.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "TOTAL AMOUNT",
                        style = MaterialTheme.typography.labelMedium.copy(color = VibrantCyan, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "₹${fmt.format(totalAmount)}",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Net / Base Price:", style = MaterialTheme.typography.bodyMedium)
                        Text("₹${fmt.format(netAmount)}", fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total GST (${selectedGSTRate.toInt()}%):", style = MaterialTheme.typography.bodyMedium)
                        Text("₹${fmt.format(gstAmount)}", fontWeight = FontWeight.Bold, color = ElectricPurple)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (!isInterState) {
                        val halfRate = selectedGSTRate / 2.0
                        val halfGst = gstAmount / 2.0
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("  • CGST (${halfRate}%):", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹${fmt.format(halfGst)}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("  • SGST (${halfRate}%):", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹${fmt.format(halfGst)}", style = MaterialTheme.typography.bodySmall)
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("  • IGST (${selectedGSTRate.toInt()}%):", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹${fmt.format(gstAmount)}", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        ActionButton(
                            text = "Copy GST Summary",
                            onClick = {
                                val summary = "Base: ₹${fmt.format(netAmount)} | GST (${selectedGSTRate.toInt()}%): ₹${fmt.format(gstAmount)} | Total: ₹${fmt.format(totalAmount)}"
                                PlatformUtils.copyToClipboard(context, summary)
                            }
                        )
                    }
                }
            }
        }
    }
}
