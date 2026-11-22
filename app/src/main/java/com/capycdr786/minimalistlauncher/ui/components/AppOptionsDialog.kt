package com.capycdr786.minimalistlauncher.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capycdr786.minimalistlauncher.data.AppInfo
import com.capycdr786.minimalistlauncher.ui.theme.InterFontFamily

@Composable
fun AppOptionsDialog(
    app: AppInfo,
    isOnHome: Boolean,
    onDismiss: () -> Unit,
    onToggleHome: () -> Unit,
    onAppInfo: () -> Unit,
    onChangeIcon: (() -> Unit)? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = app.label,
                fontFamily = InterFontFamily,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isOnHome) "remove from home" else "add to home",
                    fontFamily = InterFontFamily,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onToggleHome()
                            onDismiss()
                        }
                        .padding(vertical = 12.dp),
                )
                if (isOnHome && onChangeIcon != null) {
                    Text(
                        text = "change icon",
                        fontFamily = InterFontFamily,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDismiss()
                                onChangeIcon()
                            }
                            .padding(vertical = 12.dp),
                    )
                }
                Text(
                    text = "app info",
                    fontFamily = InterFontFamily,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAppInfo()
                            onDismiss()
                        }
                        .padding(vertical = 12.dp),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "cancel",
                    fontFamily = InterFontFamily,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
    )
}
