package com.capycdr786.minimalistlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capycdr786.minimalistlauncher.data.AppInfo
import com.capycdr786.minimalistlauncher.data.ThemeMode
import com.capycdr786.minimalistlauncher.ui.LauncherViewModel
import com.capycdr786.minimalistlauncher.ui.allapps.AllAppsDrawer
import com.capycdr786.minimalistlauncher.ui.components.AppOptionsDialog
import com.capycdr786.minimalistlauncher.ui.components.IconPickerDialog
import com.capycdr786.minimalistlauncher.ui.home.HomeScreen
import com.capycdr786.minimalistlauncher.ui.settings.LauncherSettingsDialog
import com.capycdr786.minimalistlauncher.ui.theme.BoringLauncherTheme
import com.capycdr786.minimalistlauncher.ui.util.onSwipeUp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val launcherViewModel: LauncherViewModel = viewModel()
            val themeMode by launcherViewModel.themeMode.collectAsState()
            val isDark = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }

            BoringLauncherTheme(darkTheme = isDark) {
                LauncherApp(
                    viewModel = launcherViewModel,
                    currentTheme = themeMode,
                )
            }
        }
    }
}

@Composable
fun LauncherApp(
    viewModel: LauncherViewModel,
    currentTheme: ThemeMode,
) {
    val homeApps by viewModel.homeApps.collectAsState()
    val allApps by viewModel.allApps.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val autoOpenKeyboard by viewModel.autoOpenKeyboard.collectAsState()
    val autoLaunchApp by viewModel.autoLaunchApp.collectAsState()
    val customIcons by viewModel.customIcons.collectAsState()

    var showAllApps by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var selectedAppForMenu by remember { mutableStateOf<AppInfo?>(null) }
    var selectedAppForIconPicker by remember { mutableStateOf<AppInfo?>(null) }

    BackHandler(enabled = true) {
        when {
            selectedAppForIconPicker != null -> selectedAppForIconPicker = null
            selectedAppForMenu != null -> selectedAppForMenu = null
            showSettingsDialog -> showSettingsDialog = false
            showAllApps -> {
                showAllApps = false
                viewModel.updateSearchQuery("")
            }
        }
    }

    val onDrawerAppClick = remember<(AppInfo) -> Unit>(viewModel) {
        { app ->
            viewModel.launchApp(app)
            showAllApps = false
            viewModel.updateSearchQuery("")
        }
    }
    val onDrawerAppLongClick = remember<(AppInfo) -> Unit> {
        { app ->
            selectedAppForMenu = app
        }
    }
    val onDismissDrawer = remember(viewModel) {
        {
            showAllApps = false
            viewModel.updateSearchQuery("")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .onSwipeUp(enabled = !showAllApps) {
                    showAllApps = true
                },
        ) {
            HomeScreen(
                homeApps = homeApps,
                customIcons = customIcons,
                onAppClick = { viewModel.launchApp(it) },
                onAppLongClick = { app ->
                    selectedAppForMenu = app
                },
                onOpenSettings = {
                    showSettingsDialog = true
                },
            )

            AllAppsDrawer(
                visible = showAllApps,
                apps = allApps,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                onAppClick = onDrawerAppClick,
                onAppLongClick = onDrawerAppLongClick,
                onDismiss = onDismissDrawer,
                autoOpenKeyboard = autoOpenKeyboard,
                autoLaunchSingleResult = autoLaunchApp,
            )
        }

        selectedAppForMenu?.let { app ->
            AppOptionsDialog(
                app = app,
                isOnHome = viewModel.isOnHome(app),
                onDismiss = { selectedAppForMenu = null },
                onToggleHome = {
                    if (viewModel.isOnHome(app)) {
                        viewModel.removeFromHome(app)
                    } else {
                        viewModel.addToHome(app)
                    }
                },
                onChangeIcon = {
                    selectedAppForIconPicker = app
                },
                onAppInfo = {
                    viewModel.openAppInfo(app)
                },
            )
        }

        selectedAppForIconPicker?.let { app ->
            IconPickerDialog(
                app = app,
                currentIconId = customIcons[app.packageName],
                onSelectIcon = { iconId ->
                    viewModel.setCustomIcon(app.packageName, iconId)
                },
                onDismiss = { selectedAppForIconPicker = null },
            )
        }

        if (showSettingsDialog) {
            LauncherSettingsDialog(
                currentTheme = currentTheme,
                onThemeChange = { viewModel.setThemeMode(it) },
                autoOpenKeyboard = autoOpenKeyboard,
                onAutoOpenKeyboardChange = { viewModel.setAutoOpenKeyboard(it) },
                autoLaunchApp = autoLaunchApp,
                onAutoLaunchAppChange = { viewModel.setAutoLaunchApp(it) },
                onDismiss = { showSettingsDialog = false },
            )
        }
    }
}
