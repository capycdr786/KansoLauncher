package com.capycdr786.minimalistlauncher.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.capycdr786.minimalistlauncher.data.AppInfo

@Composable
fun AppOptionsMenu(
    expanded: Boolean,
    appInfo: AppInfo,
    isOnHome: Boolean,
    onDismiss: () -> Unit,
    onToggleHome: () -> Unit,
    onAppInfo: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = if (isOnHome) "remove from home" else "add to home",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = {
                onToggleHome()
                onDismiss()
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = "app info",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = {
                onAppInfo()
                onDismiss()
            }
        )
    }
}
