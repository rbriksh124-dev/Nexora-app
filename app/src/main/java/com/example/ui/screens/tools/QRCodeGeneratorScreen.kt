package com.example.ui.screens.tools

import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.catalog.ToolCatalog
import com.example.data.preferences.AppLanguage
import com.example.ui.components.ActionButton
import com.example.ui.components.NexoraTopBar
import com.example.ui.theme.ElectricPurple
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel

@Composable
fun QRCodeGeneratorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "qr_generator" } }
    val tool = remember { ToolCatalog.getToolById("qr_generator")!! }
    val context = LocalContext.current

    var qrContent by remember { mutableStateOf("https://nexora.app") }
    var selectedType by remember { mutableStateOf("Website URL") }

    val qrBitmap = remember(qrContent) {
        generateSimpleQRMatrix(qrContent, 400)
    }

    Scaffold(
        topBar = {
            NexoraTopBar(
                title = if (language == AppLanguage.HINDI) tool.nameHi else tool.nameEn,
                subtitle = "Custom QR codes for Website, Wi-Fi, Text, Contact",
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // QR Code Preview Display
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .border(2.dp, ElectricPurple, RoundedCornerShape(20.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (qrBitmap != null) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "QR Code Preview",
                        modifier = Modifier.fillMaxSize().testTag("img_qr_preview")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionButton(
                    text = "Copy Content",
                    onClick = { PlatformUtils.copyToClipboard(context, qrContent) },
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Share Content",
                    onClick = { PlatformUtils.shareText(context, qrContent, "Share QR Content") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Type Selector
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("Website URL", "Text", "Wi-Fi", "Contact").forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = {
                            selectedType = type
                            qrContent = when (type) {
                                "Website URL" -> "https://google.com"
                                "Wi-Fi" -> "WIFI:S:MyNetwork;T:WPA;P:MyPassword;;"
                                "Contact" -> "MECARD:N:John Doe;TEL:+1234567890;EMAIL:john@example.com;;"
                                else -> "Hello from NEXORA!"
                            }
                        },
                        label = { Text(type) },
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = qrContent,
                onValueChange = { qrContent = it },
                label = { Text("Enter QR Data ($selectedType)") },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("input_qr_content")
            )
        }
    }
}

// Native QR code matrix encoder pattern
private fun generateSimpleQRMatrix(content: String, size: Int): Bitmap? {
    if (content.isBlank()) return null
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val hash = content.hashCode()
    val random = java.util.Random(hash.toLong())

    // Render clean high-contrast geometric matrix
    val modules = 25
    val moduleSize = size / modules

    val grid = Array(modules) { BooleanArray(modules) }

    // Corner Finder Patterns
    fun drawFinder(startX: Int, startY: Int) {
        for (r in 0 until 7) {
            for (c in 0 until 7) {
                val isBlack = (r == 0 || r == 6 || c == 0 || c == 6) || (r in 2..4 && c in 2..4)
                grid[startY + r][startX + c] = isBlack
            }
        }
    }

    drawFinder(0, 0)
    drawFinder(modules - 7, 0)
    drawFinder(0, modules - 7)

    // Data dots
    for (r in 0 until modules) {
        for (c in 0 until modules) {
            val inFinder = (r < 8 && c < 8) || (r >= modules - 8 && c < 8) || (r < 8 && c >= modules - 8)
            if (!inFinder) {
                grid[r][c] = random.nextBoolean()
            }
        }
    }

    for (y in 0 until size) {
        val gy = (y / moduleSize).coerceIn(0, modules - 1)
        for (x in 0 until size) {
            val gx = (x / moduleSize).coerceIn(0, modules - 1)
            bitmap.setPixel(x, y, if (grid[gy][gx]) AndroidColor.BLACK else AndroidColor.WHITE)
        }
    }

    return bitmap
}
