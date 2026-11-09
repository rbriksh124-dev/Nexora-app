package com.example.ui.screens.tools

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
fun CoinFlipScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "fun_coin_flip" } }
    val tool = remember { ToolCatalog.getToolById("fun_coin_flip")!! }
    val context = LocalContext.current

    var isFlipping by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("HEADS") }
    var rotationY by remember { mutableStateOf(0f) }

    val animatedRotation by animateFloatAsState(
        targetValue = rotationY,
        animationSpec = tween(durationMillis = 1000),
        finishedListener = {
            isFlipping = false
            PlatformUtils.performHapticFeedback(context)
        }
    )

    fun flip() {
        if (isFlipping) return
        isFlipping = true
        PlatformUtils.performHapticFeedback(context)
        val isHeads = Random().nextBoolean()
        resultText = if (isHeads) "HEADS" else "TAILS"
        rotationY += 1800f + (if (isHeads) 0f else 180f)
        viewModel.logHistory(tool, "Flip Coin", resultText)
    }

    Scaffold(
        topBar = {
            NexoraTopBar(
                title = if (language == AppLanguage.HINDI) tool.nameHi else tool.nameEn,
                subtitle = "Heads or Tails Randomizer",
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
            Spacer(modifier = Modifier.height(20.dp))

            // 3D Coin Representation
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer {
                        this.rotationY = animatedRotation
                        cameraDistance = 12f * density
                    }
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFFFFD700),
                                Color(0xFFB8860B)
                            )
                        )
                    )
                    .border(4.dp, Color(0xFFFFF8DC), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = resultText.take(1),
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF4A3B00)
                )
            }

            ResultCard(
                title = "COIN LANDED ON",
                resultText = resultText,
                onCopy = { PlatformUtils.copyToClipboard(context, resultText) },
                onShare = { PlatformUtils.shareText(context, "Coin Flip result: $resultText") }
            )

            ActionButton(
                text = if (isFlipping) "Flipping..." else "FLIP COIN",
                onClick = { flip() },
                modifier = Modifier.fillMaxWidth().testTag("btn_flip_coin")
            )
        }
    }
}
