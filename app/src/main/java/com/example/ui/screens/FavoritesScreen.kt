package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.catalog.ToolCatalog
import com.example.data.preferences.AppLanguage
import com.example.ui.components.NexoraTopBar
import com.example.ui.components.ToolCard
import com.example.viewmodel.MainViewModel

@Composable
fun FavoritesScreen(
    viewModel: MainViewModel,
    onNavigateToTool: (String) -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()

    val favoriteTools = remember(favorites) {
        val ids = favorites.map { it.toolId }.toSet()
        ToolCatalog.tools.filter { ids.contains(it.id) }
    }

    Scaffold(
        topBar = {
            NexoraTopBar(
                title = if (language == AppLanguage.HINDI) "पसंदीदा टूल्स" else "Favorite Tools",
                subtitle = if (language == AppLanguage.HINDI) "${favoriteTools.size} टूल्स बुकमार्क किए गए" else "${favoriteTools.size} bookmarked utilities"
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        if (favoriteTools.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "कोई पसंदीदा टूल नहीं" else "No Favorite Tools Yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "किसी भी टूल को यहां जोड़ने के लिए दिल के आइकन पर टैप करें" else "Tap the heart icon on any tool card to add it to your quick access favorites.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                items(favoriteTools) { tool ->
                    Box(modifier = Modifier.padding(vertical = 6.dp)) {
                        ToolCard(
                            tool = tool,
                            language = language,
                            onClick = { onNavigateToTool(tool.id) },
                            isFavorite = true,
                            onFavoriteToggle = {
                                viewModel.toggleFavorite(tool, true)
                            }
                        )
                    }
                }
            }
        }
    }
}
