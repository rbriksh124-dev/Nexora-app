package com.example.ui.screens.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.catalog.ToolCatalog
import com.example.data.preferences.AppLanguage
import com.example.ui.components.ActionButton
import com.example.ui.components.NexoraTopBar
import com.example.ui.components.ResultCard
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.VibrantCyan
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel
import java.io.ByteArrayOutputStream

@Composable
fun ImageCompressorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "img_compress" } }
    val tool = remember { ToolCatalog.getToolById("img_compress")!! }
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var originalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var compressedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var compressionQuality by remember { mutableStateOf(70f) }

    var origSizeBytes by remember { mutableStateOf(0) }
    var compSizeBytes by remember { mutableStateOf(0) }

    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            try {
                val stream = context.contentResolver.openInputStream(uri)
                val bytes = stream?.readBytes()
                if (bytes != null) {
                    origSizeBytes = bytes.size
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    originalBitmap = bmp

                    // Compress locally
                    val out = ByteArrayOutputStream()
                    bmp.compress(Bitmap.CompressFormat.JPEG, compressionQuality.toInt(), out)
                    val compBytes = out.toByteArray()
                    compSizeBytes = compBytes.size
                    compressedBitmap = BitmapFactory.decodeByteArray(compBytes, 0, compBytes.size)
                }
            } catch (_: Exception) {}
        }
    }

    fun recompress(quality: Float) {
        val bmp = originalBitmap ?: return
        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, quality.toInt(), out)
        val compBytes = out.toByteArray()
        compSizeBytes = compBytes.size
        compressedBitmap = BitmapFactory.decodeByteArray(compBytes, 0, compBytes.size)
    }

    Scaffold(
        topBar = {
            NexoraTopBar(
                title = if (language == AppLanguage.HINDI) tool.nameHi else tool.nameEn,
                subtitle = "Local On-Device Compression (100% Private)",
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
            ActionButton(
                text = "Pick Image to Compress",
                onClick = {
                    pickerLauncher.launch(
                        androidx.activity.result.PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().testTag("btn_pick_image")
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (compressedBitmap != null) {
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, ElectricPurple, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = compressedBitmap!!.asImageBitmap(),
                        contentDescription = "Compressed Preview",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val origKb = origSizeBytes / 1024
                val compKb = compSizeBytes / 1024
                val savingsPercent = if (origSizeBytes > 0) ((origSizeBytes - compSizeBytes) * 100) / origSizeBytes else 0

                ResultCard(
                    title = "File Size Reduced by $savingsPercent%",
                    resultText = "$origKb KB ➔ $compKb KB",
                    onCopy = { PlatformUtils.copyToClipboard(context, "Reduced from $origKb KB to $compKb KB ($savingsPercent% saved)") },
                    onShare = { PlatformUtils.shareText(context, "Compressed with NEXORA: Reduced from $origKb KB to $compKb KB ($savingsPercent% saved)") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text("Compression Quality: ${compressionQuality.toInt()}%")
                Slider(
                    value = compressionQuality,
                    onValueChange = {
                        compressionQuality = it
                        recompress(it)
                    },
                    valueRange = 10f..95f,
                    steps = 17
                )
            } else {
                Text(
                    text = "Select any photo from your gallery to instantly compress its size without losing clarity.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
