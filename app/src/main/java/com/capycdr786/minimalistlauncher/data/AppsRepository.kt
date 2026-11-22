package com.capycdr786.minimalistlauncher.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppsRepository(private val context: Context) {

    fun observeAllApps(): Flow<List<AppInfo>> = callbackFlow {
        suspend fun queryApps(): List<AppInfo> = withContext(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val pm = context.packageManager
            val resolveInfos = pm.queryIntentActivities(intent, 0)
            resolveInfos
                .asSequence()
                .mapNotNull { ri ->
                    val activityInfo = ri.activityInfo ?: return@mapNotNull null
                    if (activityInfo.packageName == context.packageName) return@mapNotNull null
                    AppInfo(
                        label = ri.loadLabel(pm).toString(),
                        packageName = activityInfo.packageName,
                        activityName = activityInfo.name
                    )
                }
                .sortedBy { it.label.lowercase() }
                .toList()
        }

        launch {
            trySend(queryApps())
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                launch {
                    trySend(queryApps())
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addDataScheme("package")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            context.registerReceiver(receiver, filter)
        }

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }
}
