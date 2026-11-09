package com.example.ui.screens.tools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.catalog.ToolCatalog
import com.example.data.preferences.AppLanguage
import com.example.ui.components.ActionButton
import com.example.ui.components.NexoraTopBar
import com.example.ui.components.ResultCard
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeCalculatorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val language by viewModel.language.collectAsState()
    val favorites by viewModel.favoritesList.collectAsState()
    val isFav = remember(favorites) { favorites.any { it.toolId == "calc_age" } }
    val tool = remember { ToolCatalog.getToolById("calc_age")!! }
    val context = LocalContext.current

    var birthYear by remember { mutableStateOf("2000") }
    var birthMonth by remember { mutableStateOf("1") }
    var birthDay by remember { mutableStateOf("15") }

    val today = remember { LocalDate.now() }

    val (ageSummary, extraStats) = remember(birthYear, birthMonth, birthDay) {
        val y = birthYear.toIntOrNull()
        val m = birthMonth.toIntOrNull()
        val d = birthDay.toIntOrNull()

        if (y != null && m != null && d != null && m in 1..12 && d in 1..31) {
            try {
                val dob = LocalDate.of(y, m, d)
                if (dob.isBefore(today) || dob.isEqual(today)) {
                    val period = Period.between(dob, today)
                    val totalDays = ChronoUnit.DAYS.between(dob, today)
                    val totalWeeks = totalDays / 7
                    val totalMonths = ChronoUnit.MONTHS.between(dob, today)

                    val nextBday = dob.withYear(today.year).let {
                        if (it.isBefore(today)) it.plusYears(1) else it
                    }
                    val daysToBday = ChronoUnit.DAYS.between(today, nextBday)

                    val summary = "${period.years} Years, ${period.months} Months, ${period.days} Days"
                    val stats = "Total Days: $totalDays | Weeks: $totalWeeks | Months: $totalMonths\nNext Birthday in: $daysToBday days"
                    Pair(summary, stats)
                } else Pair("Date in future", "")
            } catch (e: Exception) {
                Pair("Invalid Date", "")
            }
        } else {
            Pair("", "")
        }
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "Enter Date of Birth",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = birthDay,
                    onValueChange = { birthDay = it },
                    label = { Text("Day") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = birthMonth,
                    onValueChange = { birthMonth = it },
                    label = { Text("Month") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = birthYear,
                    onValueChange = { birthYear = it },
                    label = { Text("Year") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1.5f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (ageSummary.isNotEmpty()) {
                ResultCard(
                    title = "Exact Age",
                    resultText = ageSummary,
                    onCopy = { PlatformUtils.copyToClipboard(context, "$ageSummary\n$extraStats") },
                    onShare = { PlatformUtils.shareText(context, "$ageSummary\n$extraStats") }
                )

                if (extraStats.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                    ) {
                        Text(
                            text = extraStats,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(18.dp)
                        )
                    }
                }
            }
        }
    }
}
