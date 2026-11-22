package com.capycdr786.minimalistlauncher.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class HomeAppsStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun addToHome(packageName: String) {
        val current = prefs.getStringSet(KEY_HOME_APPS, emptySet()) ?: emptySet()
        prefs.edit().putStringSet(KEY_HOME_APPS, current + packageName).apply()
    }

    fun removeFromHome(packageName: String) {
        val current = prefs.getStringSet(KEY_HOME_APPS, emptySet()) ?: emptySet()
        prefs.edit().putStringSet(KEY_HOME_APPS, current - packageName).apply()
    }

    fun isOnHome(packageName: String): Boolean {
        val current = prefs.getStringSet(KEY_HOME_APPS, emptySet()) ?: emptySet()
        return packageName in current
    }

    fun observeHomePackages(): Flow<Set<String>> = callbackFlow {
        trySend(prefs.getStringSet(KEY_HOME_APPS, emptySet()) ?: emptySet())

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_HOME_APPS) {
                trySend(prefs.getStringSet(KEY_HOME_APPS, emptySet()) ?: emptySet())
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    fun getThemeMode(): ThemeMode {
        val name = prefs.getString(KEY_THEME_MODE, ThemeMode.DARK.name) ?: ThemeMode.DARK.name
        return try {
            ThemeMode.valueOf(name)
        } catch (_: Exception) {
            ThemeMode.DARK
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
    }

    fun observeThemeMode(): Flow<ThemeMode> = callbackFlow {
        trySend(getThemeMode())

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_THEME_MODE) {
                trySend(getThemeMode())
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    fun isAutoOpenKeyboard(): Boolean {
        return prefs.getBoolean(KEY_AUTO_OPEN_KEYBOARD, false)
    }

    fun setAutoOpenKeyboard(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_OPEN_KEYBOARD, enabled).apply()
    }

    fun observeAutoOpenKeyboard(): Flow<Boolean> = callbackFlow {
        trySend(isAutoOpenKeyboard())

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_AUTO_OPEN_KEYBOARD) {
                trySend(isAutoOpenKeyboard())
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    fun isAutoLaunchApp(): Boolean {
        return prefs.getBoolean(KEY_AUTO_LAUNCH_APP, true)
    }

    fun setAutoLaunchApp(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_LAUNCH_APP, enabled).apply()
    }

    fun observeAutoLaunchApp(): Flow<Boolean> = callbackFlow {
        trySend(isAutoLaunchApp())

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_AUTO_LAUNCH_APP) {
                trySend(isAutoLaunchApp())
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    fun getCustomIcons(): Map<String, String> {
        val raw = prefs.getString(KEY_CUSTOM_ICONS, null) ?: return emptyMap()
        return try {
            val json = org.json.JSONObject(raw)
            val map = mutableMapOf<String, String>()
            val keys = json.keys()
            while (keys.hasNext()) {
                val k = keys.next()
                map[k] = json.getString(k)
            }
            map
        } catch (_: Exception) {
            emptyMap()
        }
    }

    fun setCustomIcon(packageName: String, iconId: String?) {
        val map = getCustomIcons().toMutableMap()
        if (iconId == null) {
            map.remove(packageName)
        } else {
            map[packageName] = iconId
        }
        val json = org.json.JSONObject()
        for ((k, v) in map) {
            json.put(k, v)
        }
        prefs.edit().putString(KEY_CUSTOM_ICONS, json.toString()).apply()
    }

    fun observeCustomIcons(): Flow<Map<String, String>> = callbackFlow {
        trySend(getCustomIcons())

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_CUSTOM_ICONS) {
                trySend(getCustomIcons())
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    companion object {
        private const val PREFS_NAME = "boring_launcher_prefs"
        private const val KEY_HOME_APPS = "home_apps"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_AUTO_OPEN_KEYBOARD = "auto_open_keyboard"
        private const val KEY_AUTO_LAUNCH_APP = "auto_launch_app"
        private const val KEY_CUSTOM_ICONS = "custom_icons"
    }
}
