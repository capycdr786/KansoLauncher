package com.capycdr786.minimalistlauncher.data

import android.content.ComponentName
import androidx.compose.runtime.Immutable

@Immutable
data class AppInfo(
    val label: String,
    val packageName: String,
    val activityName: String,
    val id: String = "$packageName/$activityName",
) {
    val componentName: ComponentName
        get() = ComponentName(packageName, activityName)
}
