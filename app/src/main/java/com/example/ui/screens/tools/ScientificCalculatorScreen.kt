package com.example.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.catalog.ToolCatalog
import com.example.data.preferences.AppLanguage
import com.example.ui.components.NexoraTopBar
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.VibrantCyan
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel
import java.text.DecimalFormat
import kotlin.math.*

@Composable
fun ScientificCalculatorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "calc_scientific" } }
    val tool = remember { ToolCatalog.getToolById("calc_scientific")!! }
    val context = LocalContext.current

    var display by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }
    var isRad by remember { mutableStateOf(true) }

    fun applySciFunction(func: String) {
        PlatformUtils.performHapticFeedback(context)
        val num = display.toDoubleOrNull() ?: return
        val res = when (func) {
            "sin" -> sin(if (isRad) num else Math.toRadians(num))
            "cos" -> cos(if (isRad) num else Math.toRadians(num))
            "tan" -> tan(if (isRad) num else Math.toRadians(num))
            "ln" -> if (num > 0) ln(num) else Double.NaN
            "log" -> if (num > 0) log10(num) else Double.NaN
            "√" -> if (num >= 0) sqrt(num) else Double.NaN
            "x²" -> num * num
            "1/x" -> if (num != 0.0) 1.0 / num else Double.NaN
            "π" -> Math.PI
            "e" -> Math.E
            "n!" -> {
                val n = num.toInt()
                if (n in 0..170) {
                    var f = 1.0
                    for (i in 2..n) f *= i
                    f
                } else Double.NaN
            }
            else -> num
        }

        val formatted = if (res.isNaN()) "Error" else DecimalFormat("#.########").format(res)
        viewModel.logHistory(tool, "$func($display)", formatted)
        expression = "$func($display)"
        display = formatted
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
            // Display
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
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = isRad,
                            onClick = { isRad = !isRad },
                            label = { Text(if (isRad) "RAD" else "DEG") }
                        )
                        Text(
                            text = expression,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = display,
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            // Scientific Keypad
            val sciButtons = listOf(
                "sin", "cos", "tan", "ln", "log",
                "√", "x²", "1/x", "π", "e",
                "n!", "C", "DEL", "(", ")",
                "7", "8", "9", "÷", "±",
                "4", "5", "6", "×", "%",
                "1", "2", "3", "-", "=",
                "0", ".", "+", "EXP", "ANS"
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth().height(420.dp)
            ) {
                items(sciButtons) { btn ->
                    val isSci = btn in listOf("sin", "cos", "tan", "ln", "log", "√", "x²", "1/x", "π", "e", "n!", "EXP", "ANS")
                    val isOp = btn in listOf("÷", "×", "-", "+", "=")
                    val btnBg = when {
                        btn == "=" -> ElectricPurple
                        isOp -> VibrantCyan.copy(alpha = 0.2f)
                        isSci -> MaterialTheme.colorScheme.surfaceVariant
                        else -> MaterialTheme.colorScheme.surface
                    }
                    val textTint = when {
                        btn == "=" -> Color.White
                        isOp -> VibrantCyan
                        isSci -> ElectricPurple
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                    Box(
                        modifier = Modifier
                            .aspectRatio(1.2f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(btnBg)
                            .clickable {
                                when (btn) {
                                    "C" -> {
                                        PlatformUtils.performHapticFeedback(context)
                                        display = "0"
                                        expression = ""
                                    }
                                    "DEL" -> {
                                        PlatformUtils.performHapticFeedback(context)
                                        display = if (display.length > 1) display.dropLast(1) else "0"
                                    }
                                    in listOf("sin", "cos", "tan", "ln", "log", "√", "x²", "1/x", "π", "e", "n!") -> {
                                        applySciFunction(btn)
                                    }
                                    "=" -> {
                                        PlatformUtils.performHapticFeedback(context)
                                        // Evaluate basic pending if present
                                        display = display
                                    }
                                    "." -> {
                                        PlatformUtils.performHapticFeedback(context)
                                        if (!display.contains(".")) display += "."
                                    }
                                    else -> {
                                        PlatformUtils.performHapticFeedback(context)
                                        if (display == "0") display = btn else display += btn
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = btn,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = textTint
                        )
                    }
                }
            }
        }
    }
}
