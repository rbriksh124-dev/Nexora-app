package com.example.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class ToolCategory(val id: String, val titleEn: String, val titleHi: String, val icon: ImageVector) {
    CALCULATORS("calculators", "Calculators", "कैलकुलेटर", Icons.Default.Calculate),
    CONVERTERS("converters", "Unit Converters", "इकाई परिवर्तक", Icons.Default.SwapHoriz),
    TEXT_TOOLS("text_tools", "Text Utilities", "टेक्स्ट उपकरण", Icons.Default.TextFields),
    QR_BARCODE("qr_barcode", "QR & Barcode", "क्यूआर और बारकोड", Icons.Default.QrCode),
    IMAGE_TOOLS("image_tools", "Image Tools", "छवि उपकरण", Icons.Default.Image),
    DEV_TOOLS("dev_tools", "Developer Tools", "डेवलपर उपकरण", Icons.Default.Code),
    PRODUCTIVITY("productivity", "Productivity", "उत्पादकता", Icons.Default.CheckCircle),
    DAILY_LIFE("daily_life", "Daily Life", "दैनिक जीवन", Icons.Default.Schedule),
    FUN_RANDOM("fun_random", "Fun & Random", "मनोरंजन और यादृच्छिक", Icons.Default.Casino)
}

data class UtilityTool(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val category: ToolCategory,
    val icon: ImageVector,
    val keywords: List<String> = emptyList(),
    val isPopular: Boolean = false,
    val isFeatured: Boolean = false
)
