package com.capycdr786.minimalistlauncher.ui.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capycdr786.minimalistlauncher.data.ThemeMode
import com.capycdr786.minimalistlauncher.ui.theme.LauncherFontFamily

@Composable
fun LauncherSettingsDialog(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    autoOpenKeyboard: Boolean,
    onAutoOpenKeyboardChange: (Boolean) -> Unit,
    autoLaunchApp: Boolean,
    onAutoLaunchAppChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "settings",
                fontFamily = LauncherFontFamily,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "theme",
                        fontFamily = LauncherFontFamily,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    )
                    ThemeThreeStateButton(
                        currentTheme = currentTheme,
                        onThemeSelect = onThemeChange,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAutoOpenKeyboardChange(!autoOpenKeyboard) }
                        .padding(vertical = 4.dp),
                ) {
                    Text(
                        text = "auto open keyboard",
                        fontFamily = LauncherFontFamily,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    RoundedToggleButton(
                        enabled = autoOpenKeyboard,
                        onToggle = { onAutoOpenKeyboardChange(!autoOpenKeyboard) },
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAutoLaunchAppChange(!autoLaunchApp) }
                        .padding(vertical = 4.dp),
                ) {
                    Text(
                        text = "auto launch single result",
                        fontFamily = LauncherFontFamily,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    RoundedToggleButton(
                        enabled = autoLaunchApp,
                        onToggle = { onAutoLaunchAppChange(!autoLaunchApp) },
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                        .clickable {
                            openHomeSettings(context)
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "set as default launcher",
                        fontFamily = LauncherFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Text(
                    text = "version 1.0.0",
                    fontFamily = LauncherFontFamily,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    .clickable(onClick = onDismiss)
                    .padding(horizontal = 20.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "close",
                    fontFamily = LauncherFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
    )
}

@Composable
private fun ThemeThreeStateButton(
    currentTheme: ThemeMode,
    onThemeSelect: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(
        ThemeMode.LIGHT to "light",
        ThemeMode.DARK to "dark",
        ThemeMode.SYSTEM to "system",
    )
    val containerBg = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    val containerShape = RoundedCornerShape(14.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(containerShape)
            .background(containerBg)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEach { (mode, label) ->
            val isSelected = currentTheme == mode
            val itemShape = RoundedCornerShape(10.dp)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(itemShape)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            Color.Transparent
                        }
                    )
                    .clickable { onThemeSelect(mode) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    fontFamily = LauncherFontFamily,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    },
                )
            }
        }
    }
}

@Composable
private fun RoundedToggleButton(
    enabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)
    val bgColor = if (enabled) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    }
    val textColor = if (enabled) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    }

    Box(
        modifier = modifier
            .width(58.dp)
            .height(32.dp)
            .clip(shape)
            .background(bgColor)
            .clickable(onClick = onToggle),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (enabled) "on" else "off",
            fontFamily = LauncherFontFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
        )
    }
}

private fun openHomeSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_HOME_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        val intent = Intent(Settings.ACTION_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
