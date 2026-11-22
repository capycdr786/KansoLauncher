package com.capycdr786.minimalistlauncher.ui.allapps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capycdr786.minimalistlauncher.data.AppInfo
import com.capycdr786.minimalistlauncher.ui.theme.InterFontFamily
import com.capycdr786.minimalistlauncher.ui.util.onSwipeDown
import kotlinx.coroutines.delay

@Composable
fun AllAppsDrawer(
    visible: Boolean,
    apps: List<AppInfo>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    onDismiss: () -> Unit,
    autoOpenKeyboard: Boolean = false,
    autoLaunchSingleResult: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(apps, searchQuery, visible, autoLaunchSingleResult) {
        if (visible && autoLaunchSingleResult && searchQuery.trim().isNotEmpty() && apps.size == 1) {
            delay(150)
            if (apps.size == 1 && searchQuery.trim().isNotEmpty()) {
                onAppClick(apps.first())
            }
        }
    }

    LaunchedEffect(visible, autoOpenKeyboard) {
        if (visible) {
            listState.scrollToItem(0)
            if (autoOpenKeyboard) {
                delay(60)
                try {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                } catch (_: Exception) {}
            }
        } else {
            keyboardController?.hide()
        }
    }

    val nestedScrollConnection = remember(onDismiss) {
        object : NestedScrollConnection {
            var accumulatedDown = 0f

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < 0f) {
                    accumulatedDown = 0f
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                if (source == NestedScrollSource.UserInput && available.y > 0f) {
                    accumulatedDown += available.y
                    if (accumulatedDown > 24f) {
                        accumulatedDown = 0f
                        onDismiss()
                    }
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (available.y > 100f) {
                    onDismiss()
                }
                return Velocity.Zero
            }
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 80),
        ) + slideInVertically(
            initialOffsetY = { 60 },
            animationSpec = tween(durationMillis = 80),
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 50),
        ),
        modifier = modifier,
    ) {
        val textColor = MaterialTheme.colorScheme.onBackground
        val textStyle = MaterialTheme.typography.bodyLarge

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .nestedScroll(nestedScrollConnection)
                .padding(top = 12.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .onSwipeDown { onDismiss() },
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .background(
                            color = textColor.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(2.dp),
                        ),
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSwipeDown { onDismiss() },
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = "search apps...",
                            fontFamily = InterFontFamily,
                            color = textColor.copy(alpha = 0.4f),
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = InterFontFamily,
                        fontSize = 16.sp,
                        color = textColor,
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = textColor.copy(alpha = 0.2f),
                        unfocusedIndicatorColor = textColor.copy(alpha = 0.1f),
                        cursorColor = textColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (apps.isNotEmpty()) {
                                onAppClick(apps.first())
                            }
                        },
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp)
                        .focusRequester(focusRequester),
                )
            }

            val wheelFlingBehavior = com.capycdr786.minimalistlauncher.ui.util.rememberWheelFlingBehavior()

            LazyColumn(
                state = listState,
                flingBehavior = wheelFlingBehavior,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 28.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                items(
                    items = apps,
                    key = { it.id },
                    contentType = { "all_apps_item" },
                ) { app ->
                    AppDrawerItem(
                        app = app,
                        onAppClick = onAppClick,
                        onAppLongClick = onAppLongClick,
                        textColor = textColor,
                        textStyle = textStyle,
                    )
                }
            }
        }
    }
}

@Composable
private fun AppDrawerItem(
    app: AppInfo,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    textColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    val onClick = remember(app, onAppClick) { { onAppClick(app) } }
    val onLongClick = remember(app, onAppLongClick) { { onAppLongClick(app) } }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .combinedClickable(
                interactionSource = null,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = app.label,
            style = textStyle,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
