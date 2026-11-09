package com.example.ui.screens.tools

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
import java.util.Random

@Composable
fun SpinTheWheelScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "fun_spin_wheel" } }
    val tool = remember { ToolCatalog.getToolById("fun_spin_wheel")!! }
    val context = LocalContext.current

    val items = remember { mutableStateListOf("Pizza", "Burger", "Sushi", "Salad", "Tacos", "Pasta") }
    var selectedResult by remember { mutableStateOf<String?>(null) }
    var rotationAngle by remember { mutableStateOf(0f) }
    var isSpinning by remember { mutableStateOf(false) }

    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 3000),
        finishedListener = {
            isSpinning = false
            // Calculate picked slice
            val sliceDeg = 360f / items.size
            val normalized = (rotationAngle % 360f + 360f) % 360f
            // Pointer at top (270 degrees)
            val pointerDeg = (360f - normalized + 270f) % 360f
            val idx = (pointerDeg / sliceDeg).toInt() % items.size
            selectedResult = items[idx]
            PlatformUtils.performHapticFeedback(context)
        }
    )

    fun spin() {
        if (isSpinning || items.isEmpty()) return
        isSpinning = true
        selectedResult = null
        PlatformUtils.performHapticFeedback(context)
        val spins = 5 + Random().nextInt(5)
        val extraDeg = Random().nextFloat() * 360f
        rotationAngle += (spins * 360f) + extraDeg
    }

    val sliceColors = listOf(
        Color(0xFFA855F7), Color(0xFF06B6D4), Color(0xFF10B981),
        Color(0xFFF59E0B), Color(0xFFEC4899), Color(0xFF3B82F6)
    )

    Scaffold(
        topBar = {
            NexoraTopBar(
                title = if (language == AppLanguage.HINDI) tool.nameHi else tool.nameEn,
                subtitle = "Random Decision Maker Wheel",
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Spin Wheel Canvas with Pointer
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(animatedRotation)
                        .testTag("canvas_spin_wheel")
                ) {
                    val count = items.size
                    val sweep = 360f / count
                    for (i in 0 until count) {
                        drawArc(
                            color = sliceColors[i % sliceColors.size],
                            startAngle = i * sweep,
                            sweepAngle = sweep,
                            useCenter = true,
                            size = size
                        )
                    }
                    drawCircle(color = Color(0xFF0B0E14), radius = 24.dp.toPx())
                }

                // Top Pointer Needle
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.TopCenter)
                        .background(Color.White, CircleShape)
                        .border(2.dp, ElectricPurple, CircleShape)
                )
            }

            if (selectedResult != null) {
                ResultCard(
                    title = "THE WHEEL CHOSE",
                    resultText = selectedResult!!,
                    onCopy = { PlatformUtils.copyToClipboard(context, selectedResult!!) },
                    onShare = { PlatformUtils.shareText(context, "The Wheel Chose: $selectedResult!") }
                )
            } else {
                Text(
                    text = if (isSpinning) "Spinning..." else "Tap Spin to randomly pick an option!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ActionButton(
                text = if (isSpinning) "Spinning..." else "SPIN THE WHEEL",
                onClick = { spin() },
                modifier = Modifier.fillMaxWidth().testTag("btn_spin_wheel")
            )
        }
    }
}
