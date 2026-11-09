package com.example.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.data.preferences.AppLanguage
import com.example.ui.screens.*
import com.example.ui.screens.tools.*
import com.example.ui.theme.ElectricPurple
import com.example.viewmodel.MainViewModel

sealed class Screen(val route: String, val titleEn: String, val titleHi: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("dashboard", "Home", "होम", Icons.Default.Home)
    object Explore : Screen("explore", "Explore", "एक्सप्लोर", Icons.Default.Explore)
    object Favorites : Screen("favorites", "Favorites", "पसंदीदा", Icons.Default.Favorite)
    object History : Screen("history", "History", "इतिहास", Icons.Default.History)
    object Settings : Screen("settings", "Settings", "सेटिंग्स", Icons.Default.Settings)
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val language by viewModel.language.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Explore,
        Screen.Favorites,
        Screen.History,
        Screen.Settings
    )

    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(Screen.Home.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(imageVector = screen.icon, contentDescription = screen.titleEn) },
                            label = { Text(if (language == AppLanguage.HINDI) screen.titleHi else screen.titleEn) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                indicatorColor = ElectricPurple
                            ),
                            modifier = Modifier.testTag("nav_item_${screen.route}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToTool = { toolId -> navController.navigate("tool/$toolId") },
                    onNavigateToExplore = { navController.navigate(Screen.Explore.route) }
                )
            }

            composable(Screen.Explore.route) {
                ExploreScreen(
                    viewModel = viewModel,
                    onNavigateToTool = { toolId -> navController.navigate("tool/$toolId") }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    viewModel = viewModel,
                    onNavigateToTool = { toolId -> navController.navigate("tool/$toolId") }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = viewModel,
                    onNavigateToTool = { toolId -> navController.navigate("tool/$toolId") }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = viewModel)
            }

            composable("auth_login") {
                val context = androidx.compose.ui.platform.LocalContext.current
                val authViewModel = androidx.lifecycle.viewmodel.compose.viewModel<com.example.viewmodel.AuthViewModel>()
                LoginScreen(
                    authViewModel = authViewModel,
                    onNavigateToSignUp = { navController.navigate("auth_signup") },
                    onLoginSuccess = { navController.popBackStack() },
                    onSkipOrContinueOffline = { navController.popBackStack() }
                )
            }

            composable("auth_signup") {
                val authViewModel = androidx.lifecycle.viewmodel.compose.viewModel<com.example.viewmodel.AuthViewModel>()
                SignUpScreen(
                    authViewModel = authViewModel,
                    onNavigateToLogin = { navController.popBackStack() },
                    onSignUpSuccess = { navController.popBackStack() },
                    onSkipOrContinueOffline = { navController.popBackStack() }
                )
            }

            // Dedicated Individual Tool Screens
            composable("tool/calc_standard") {
                StandardCalculatorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/calc_scientific") {
                ScientificCalculatorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/calc_percentage") {
                PercentageCalculatorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/calc_discount") {
                DiscountCalculatorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/calc_gst") {
                GSTCalculatorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/calc_emi") {
                EMICalculatorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/calc_age") {
                AgeCalculatorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/calc_bmi") {
                BMICalculatorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/conv_currency") {
                CurrencyConverterScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/text_counter") {
                TextCounterScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/text_password") {
                PasswordGeneratorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/qr_generator") {
                QRCodeGeneratorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/img_compress") {
                ImageCompressorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/dev_json") {
                JSONFormatterScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/prod_todo") {
                TodoListScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/fun_spin_wheel") {
                SpinTheWheelScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable("tool/fun_coin_flip") {
                CoinFlipScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            // Dynamic Fallback Converter / Tool Screen for remaining unit types
            composable("tool/{toolId}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("toolId") ?: "conv_length"
                UnitConverterScreen(viewModel = viewModel, toolId = id, onBack = { navController.popBackStack() })
            }
        }
    }
}
