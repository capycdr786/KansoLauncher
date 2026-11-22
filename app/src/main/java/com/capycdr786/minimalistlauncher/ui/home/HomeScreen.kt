package com.capycdr786.minimalistlauncher.ui.home

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.capycdr786.minimalistlauncher.data.AppInfo
import com.capycdr786.minimalistlauncher.ui.theme.InterFontFamily
import com.capycdr786.minimalistlauncher.ui.util.AppIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    homeApps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    customIcons: Map<String, String> = emptyMap(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onOpenSettings() },
                )
            },
    ) {
        HomeClock(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 28.dp, top = 20.dp, bottom = 4.dp),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            if (homeApps.isNotEmpty()) {
                val wheelFlingBehavior = com.capycdr786.minimalistlauncher.ui.util.rememberWheelFlingBehavior()
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    flingBehavior = wheelFlingBehavior,
                    contentPadding = PaddingValues(horizontal = 28.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    items(
                        items = homeApps,
                        key = { it.id },
                        contentType = { "home_app" },
                    ) { app ->
                        HomeAppRow(
                            app = app,
                            customIconId = customIcons[app.packageName],
                            onClick = { onAppClick(app) },
                            onLongClick = { onAppLongClick(app) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeAppRow(
    app: AppInfo,
    customIconId: String?,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .combinedClickable(
                interactionSource = null,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick,
            ),
    ) {
        Text(
            text = app.label,
            fontFamily = InterFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
            maxLines = 1,
        )
        Spacer(modifier = Modifier.width(16.dp))
        AppIcon(
            packageName = app.packageName,
            label = app.label,
            activityName = app.activityName,
            customIconId = customIconId,
            size = 22.dp,
        )
    }
}

@Composable
fun HomeClock(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            while (isActive) {
                val now = System.currentTimeMillis()
                currentTime = now
                val millisToNextMinute = 60_000L - (now % 60_000L) + 50L
                delay(millisToNextMinute)
            }
        }
    }

    val is24Hour = try {
        DateFormat.is24HourFormat(context)
    } catch (_: Exception) {
        false
    }

    val timePattern = if (is24Hour) "HH:mm" else "h:mm"
    val datePattern = "EEEE, MMMM d"

    val timeString = remember(currentTime, is24Hour) {
        SimpleDateFormat(timePattern, Locale.getDefault()).format(Date(currentTime))
    }
    val dateString = remember(currentTime) {
        SimpleDateFormat(datePattern, Locale.getDefault()).format(Date(currentTime))
    }

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier,
    ) {
        Text(
            text = timeString,
            fontFamily = InterFontFamily,
            fontSize = 54.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { openClockApp(context) },
            ),
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = dateString,
            fontFamily = InterFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
    }
}

private fun openClockApp(context: Context) {
    val intents = listOf(
        Intent(AlarmClock.ACTION_SHOW_ALARMS),
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setPackage("com.google.android.deskclock"),
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setPackage("com.android.deskclock"),
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setPackage("com.miui.deskclock"),
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setPackage("com.sec.android.app.clockpackage"),
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setPackage("com.coloros.alarmclock"),
    )
    for (intent in intents) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                return
            }
        } catch (_: Exception) {}
    }
}
