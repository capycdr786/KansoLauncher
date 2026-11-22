package com.capycdr786.minimalistlauncher.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.capycdr786.minimalistlauncher.data.AppInfo
import com.capycdr786.minimalistlauncher.data.AppsRepository
import com.capycdr786.minimalistlauncher.data.HomeAppsStore
import com.capycdr786.minimalistlauncher.data.ThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val appsRepository = AppsRepository(application)
    private val homeAppsStore = HomeAppsStore(application)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val allAppsFlow = appsRepository.observeAllApps()
    private val homePackagesFlow = homeAppsStore.observeHomePackages()

    val allApps: StateFlow<List<AppInfo>> = combine(
        allAppsFlow,
        _searchQuery
    ) { apps, query ->
        if (query.isBlank()) apps
        else apps.filter { it.label.contains(query, ignoreCase = true) }
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val homeApps: StateFlow<List<AppInfo>> = combine(
        allAppsFlow,
        homePackagesFlow
    ) { apps, homePackages ->
        apps.filter { it.packageName in homePackages }
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val themeMode: StateFlow<ThemeMode> = homeAppsStore.observeThemeMode()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), homeAppsStore.getThemeMode())

    val autoOpenKeyboard: StateFlow<Boolean> = homeAppsStore.observeAutoOpenKeyboard()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), homeAppsStore.isAutoOpenKeyboard())

    val autoLaunchApp: StateFlow<Boolean> = homeAppsStore.observeAutoLaunchApp()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), homeAppsStore.isAutoLaunchApp())

    val customIcons: StateFlow<Map<String, String>> = homeAppsStore.observeCustomIcons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), homeAppsStore.getCustomIcons())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setThemeMode(mode: ThemeMode) {
        homeAppsStore.setThemeMode(mode)
    }

    fun setAutoOpenKeyboard(enabled: Boolean) {
        homeAppsStore.setAutoOpenKeyboard(enabled)
    }

    fun setAutoLaunchApp(enabled: Boolean) {
        homeAppsStore.setAutoLaunchApp(enabled)
    }

    fun setCustomIcon(packageName: String, iconId: String?) {
        homeAppsStore.setCustomIcon(packageName, iconId)
    }

    fun launchApp(appInfo: AppInfo) {
        try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                component = appInfo.componentName
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            }
            getApplication<Application>().startActivity(intent)
        } catch (_: Exception) {}
    }

    fun openAppInfo(appInfo: AppInfo) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${appInfo.packageName}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            getApplication<Application>().startActivity(intent)
        } catch (_: Exception) {}
    }

    fun addToHome(appInfo: AppInfo) {
        homeAppsStore.addToHome(appInfo.packageName)
    }

    fun removeFromHome(appInfo: AppInfo) {
        homeAppsStore.removeFromHome(appInfo.packageName)
    }

    fun isOnHome(appInfo: AppInfo): Boolean {
        return homeAppsStore.isOnHome(appInfo.packageName)
    }
}
