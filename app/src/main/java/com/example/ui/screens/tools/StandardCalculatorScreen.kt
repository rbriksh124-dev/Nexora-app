package com.example.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import kotlin.math.*

@Composable
fun StandardCalculatorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "calc_standard" } }
    val tool = remember { ToolCatalog.getToolById("calc_standard")!! }
    val context = LocalContext.current

    var display by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }
    var lastOperator by remember { mutableStateOf<String?>(null) }
    var firstOperand by remember { mutableStateOf<Double?>(null) }
    var resetOnNextDigit by remember { mutableStateOf(false) }

    fun onDigit(d: String) {
        PlatformUtils.performHapticFeedback(context)
        if (resetOnNextDigit || display == "0") {
            display = d
            resetOnNextDigit = false
        } else {
            display += d
        }
    }

    fun onOperator(op: String) {
        PlatformUtils.performHapticFeedback(context)
        val currentVal = display.toDoubleOrNull() ?: 0.0
        firstOperand = currentVal
        lastOperator = op
        expression = "$display $op"
        resetOnNextDigit = true
    }

    fun onEquals() {
        PlatformUtils.performHapticFeedback(context)
        val op = lastOperator ?: return
        val num1 = firstOperand ?: return
        val num2 = display.toDoubleOrNull() ?: return

        val res = when (op) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "÷" -> if (num2 != 0.0) num1 / num2 else Double.NaN
            else -> num2
        }

        val formattedRes = if (res.isNaN()) "Error" else DecimalFormat("#.########").format(res)
        viewModel.logHistory(tool, "$expression $display", formattedRes)
        expression = "$expression $display ="
        display = formattedRes
        firstOperand = null
        lastOperator = null
        resetOnNextDigit = true
    }

    fun onClear() {
        PlatformUtils.performHapticFeedback(context)
        display = "0"
        expression = ""
        firstOperand = null
        lastOperator = null
        resetOnNextDigit = false
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
                .padding(16.dp)
        ) {
            // Calculator Screen Display
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(bottom = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = expression,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = display,
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        modifier = Modifier.testTag("text_calc_display")
                    )
                }
            }

            // Keypad Grid
            val buttons = listOf(
                "C", "±", "%", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ".", "DEL", "="
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().height(360.dp)
            ) {
                items(buttons) { btn ->
                    val isOp = btn in listOf("÷", "×", "-", "+", "=")
                    val isAction = btn in listOf("C", "±", "%", "DEL")

                    val btnColor = when {
                        btn == "=" -> ElectricPurple
                        isOp -> VibrantCyan.copy(alpha = 0.2f)
                        isAction -> MaterialTheme.colorScheme.surfaceVariant
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val textColor = when {
                        btn == "=" -> Color.White
                        isOp -> VibrantCyan
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                    Box(
                        modifier = Modifier
                            .aspectRatio(1.1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(btnColor)
                            .clickable {
                                when (btn) {
                                    "C" -> onClear()
                                    "DEL" -> {
                                        PlatformUtils.performHapticFeedback(context)
                                        display = if (display.length > 1) display.dropLast(1) else "0"
                                    }
                                    "±" -> {
                                        PlatformUtils.performHapticFeedback(context)
                                        val d = display.toDoubleOrNull()
                                        if (d != null) display = DecimalFormat("#.########").format(-d)
                                    }
                                    "%" -> {
                                        PlatformUtils.performHapticFeedback(context)
                                        val d = display.toDoubleOrNull()
                                        if (d != null) display = DecimalFormat("#.########").format(d / 100.0)
                                    }
                                    in listOf("÷", "×", "-", "+") -> onOperator(btn)
                                    "=" -> onEquals()
                                    "." -> {
                                        PlatformUtils.performHapticFeedback(context)
                                        if (!display.contains(".")) display += "."
                                    }
                                    else -> onDigit(btn)
                                }
                            }
                            .testTag("calc_btn_$btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = btn,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = if (isOp || btn == "=") FontWeight.Bold else FontWeight.Medium
                            ),
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}
