package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.sp
import com.example.data.catalog.ToolCatalog
import com.example.data.models.ToolCategory
import com.example.data.preferences.AppLanguage
import com.example.ui.components.NexoraTopBar
import com.example.ui.components.ToolCard
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.VibrantCyan
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel

@Composable
fun ExploreScreen(
    viewModel: MainViewModel,
    onNavigateToTool: (String) -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val favoriteToolIds = remember(favorites) { favorites.map { it.toolId }.toSet() }
    val context = LocalContext.current

    var selectedCategory by remember { mutableStateOf<ToolCategory?>(null) }

    val filteredTools = remember(selectedCategory) {
        if (selectedCategory == null) ToolCatalog.tools
        else ToolCatalog.tools.filter { it.category == selectedCategory }
    }

    Scaffold(
        topBar = {
            NexoraTopBar(
                title = if (language == AppLanguage.HINDI) "टूल एक्सप्लोरर" else "Explore All Tools",
                subtitle = if (language == AppLanguage.HINDI) "${ToolCatalog.tools.size} उपयोगी टूल्स उपलब्ध" else "${ToolCatalog.tools.size} premium utilities available"
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Category Filter Pills
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = {
                                PlatformUtils.performHapticFeedback(context)
                                selectedCategory = null
                            },
                            label = { Text(if (language == AppLanguage.HINDI) "सभी (${ToolCatalog.tools.size})" else "All (${ToolCatalog.tools.size})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricPurple,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    items(ToolCategory.values()) { category ->
                        val count = remember(category) { ToolCatalog.tools.count { it.category == category } }
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = {
                                PlatformUtils.performHapticFeedback(context)
                                selectedCategory = category
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = if (language == AppLanguage.HINDI) "${category.titleHi} ($count)" else "${category.titleEn} ($count)"
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricPurple,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Tools list in category
            items(filteredTools) { tool ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    ToolCard(
                        tool = tool,
                        language = language,
                        onClick = { onNavigateToTool(tool.id) },
                        isFavorite = favoriteToolIds.contains(tool.id),
                        onFavoriteToggle = {
                            viewModel.toggleFavorite(tool, favoriteToolIds.contains(tool.id))
                        }
                    )
                }
            }
        }
    }
}
