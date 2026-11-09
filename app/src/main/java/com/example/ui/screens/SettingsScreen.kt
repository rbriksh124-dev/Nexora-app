package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.preferences.AppLanguage
import com.example.data.preferences.AppThemeMode
import com.example.ui.components.NexoraTopBar
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.VibrantCyan
import com.example.utils.PlatformUtils
import com.example.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel
) {
    val language by viewModel.language.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val hapticEnabled by viewModel.hapticEnabled.collectAsState()
    val context = LocalContext.current

    var showClearDataDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NexoraTopBar(
                title = if (language == AppLanguage.HINDI) "सेटिंग्स" else "Settings",
                subtitle = if (language == AppLanguage.HINDI) "प्राथमिकताएं और डेटा प्रबंधन" else "Preferences & Data Management"
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        if (showClearDataDialog) {
            AlertDialog(
                onDismissRequest = { showClearDataDialog = false },
                title = { Text(if (language == AppLanguage.HINDI) "सभी स्थानीय डेटा मिटाएं?" else "Reset All Local Data?") },
                text = { Text(if (language == AppLanguage.HINDI) "यह आपके सभी कार्य, नोट्स, खर्च और इतिहास रिकॉर्ड मिटा देगा। यह क्रिया पूर्ववत नहीं की जा सकती।" else "This will permanently delete all your local tasks, notes, expense entries, and history. This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearAllData()
                            showClearDataDialog = false
                            PlatformUtils.performHapticFeedback(context)
                        }
                    ) {
                        Text(if (language == AppLanguage.HINDI) "डेटा मिटाएं" else "Reset Data", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDataDialog = false }) {
                        Text(if (language == AppLanguage.HINDI) "रद्द करें" else "Cancel")
                    }
                }
            )
        }

        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("About NEXORA") },
                text = {
                    Column {
                        Text("NEXORA — Premium All-In-One Android Utility Super App", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Tagline: One App. Endless Possibilities.", color = VibrantCyan)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Designed for Gen Z, students, and everyday power users. Built natively with 100% Kotlin, Jetpack Compose, Room Database, and Firebase Firestore cloud sync.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Version: 1.0.0 (Build 1)")
                        Text("Status: Production Grade")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                val user by viewModel.currentUser.collectAsState()
                val isLoading by viewModel.authManager.isLoading.collectAsState()
                val coroutineScope = rememberCoroutineScope()
                val activity = context as? android.app.Activity

                Text(
                    text = if (language == AppLanguage.HINDI) "खाता और क्लाउड सिंक" else "Account & Cloud Sync",
                    style = MaterialTheme.typography.labelLarge.copy(color = ElectricPurple, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (user != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = VibrantCyan,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = user?.displayName ?: "Google User",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = user?.email ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    viewModel.authManager.signOut(coroutineScope)
                                    PlatformUtils.performHapticFeedback(context)
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth().testTag("btn_auth_sign_out")
                            ) {
                                Text("Sign Out", color = MaterialTheme.colorScheme.onSurface)
                            }
                        } else {
                            Text(
                                text = "Sign in with Google to backup favorites, history, and notes to your secure cloud account.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    if (activity != null) {
                                        viewModel.authManager.signInWithGoogle(activity, coroutineScope)
                                    }
                                },
                                enabled = !isLoading,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                modifier = Modifier.fillMaxWidth().testTag("btn_auth_google_sign_in")
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Sign in with Google", color = Color.White)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = if (language == AppLanguage.HINDI) "दिखावट और थीम" else "Appearance & Theme",
                    style = MaterialTheme.typography.labelLarge.copy(color = ElectricPurple, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "डार्क मोड (गहरा रंग)" else "Dark Theme (Futuristic)",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Switch(
                                checked = themeMode == AppThemeMode.DARK,
                                onCheckedChange = { isDark ->
                                    viewModel.setTheme(if (isDark) AppThemeMode.DARK else AppThemeMode.LIGHT)
                                },
                                modifier = Modifier.testTag("switch_dark_theme")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = if (language == AppLanguage.HINDI) "भाषा चयन" else "Language Selection",
                    style = MaterialTheme.typography.labelLarge.copy(color = ElectricPurple, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setLanguage(AppLanguage.ENGLISH) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("English (Default)", style = MaterialTheme.typography.bodyLarge)
                            RadioButton(
                                selected = language == AppLanguage.ENGLISH,
                                onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) },
                                modifier = Modifier.testTag("radio_lang_en")
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setLanguage(AppLanguage.HINDI) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("हिन्दी (Hindi)", style = MaterialTheme.typography.bodyLarge)
                            RadioButton(
                                selected = language == AppLanguage.HINDI,
                                onClick = { viewModel.setLanguage(AppLanguage.HINDI) },
                                modifier = Modifier.testTag("radio_lang_hi")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = if (language == AppLanguage.HINDI) "सिस्टम और अनुभव" else "System & Feedback",
                    style = MaterialTheme.typography.labelLarge.copy(color = ElectricPurple, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "हैप्टिक कंपन प्रतिक्रिया" else "Haptic Touch Feedback",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Switch(
                                checked = hapticEnabled,
                                onCheckedChange = { viewModel.setHaptic(it) },
                                modifier = Modifier.testTag("switch_haptic")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = if (language == AppLanguage.HINDI) "डेटा और गोपनीयता" else "Data & Privacy",
                    style = MaterialTheme.typography.labelLarge.copy(color = ElectricPurple, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        SettingsRow(
                            icon = Icons.Default.DeleteOutline,
                            title = if (language == AppLanguage.HINDI) "गणना इतिहास साफ़ करें" else "Clear Calculation History",
                            onClick = { viewModel.clearHistory() }
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                        SettingsRow(
                            icon = Icons.Default.WarningAmber,
                            title = if (language == AppLanguage.HINDI) "सभी स्थानीय डेटा रीसेट करें" else "Reset All Local App Data",
                            textColor = MaterialTheme.colorScheme.error,
                            onClick = { showClearDataDialog = true }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = if (language == AppLanguage.HINDI) "ऐप के बारे में" else "About & Support",
                    style = MaterialTheme.typography.labelLarge.copy(color = ElectricPurple, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        SettingsRow(
                            icon = Icons.Default.Info,
                            title = if (language == AppLanguage.HINDI) "NEXORA के बारे में" else "About NEXORA",
                            subtitle = "v1.0.0 (Build 1)",
                            onClick = { showAboutDialog = true }
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                        SettingsRow(
                            icon = Icons.Default.Share,
                            title = if (language == AppLanguage.HINDI) "NEXORA ऐप साझा करें" else "Share NEXORA App",
                            onClick = {
                                PlatformUtils.shareText(
                                    context,
                                    "Discover NEXORA — The Premium All-in-One Android Utility Super App with 50+ tools, calculators, converters, and productivity managers!",
                                    "Share NEXORA"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, color = textColor)
            if (subtitle != null) {
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(18.dp)
        )
    }
}
