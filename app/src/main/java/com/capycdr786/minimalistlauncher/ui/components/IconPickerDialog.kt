package com.capycdr786.minimalistlauncher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capycdr786.minimalistlauncher.data.AppInfo
import com.capycdr786.minimalistlauncher.ui.theme.InterFontFamily
import com.capycdr786.minimalistlauncher.ui.util.AppIconLoader
import com.capycdr786.minimalistlauncher.ui.util.SelectableIcon

@Composable
fun IconPickerDialog(
    app: AppInfo,
    currentIconId: String?,
    onSelectIcon: (String?) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "choose icon",
                    fontFamily = InterFontFamily,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = app.label,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (currentIconId == null) {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            } else {
                                Color.Transparent
                            }
                        )
                        .clickable {
                            onSelectIcon(null)
                            onDismiss()
                        }
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "• auto-detect (default)",
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        fontWeight = if (currentIconId == null) FontWeight.Medium else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp),
                    contentPadding = PaddingValues(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(
                        items = AppIconLoader.availableIcons,
                        key = { it.id },
                    ) { iconItem ->
                        val isSelected = currentIconId == iconItem.id
                        IconGridCell(
                            iconItem = iconItem,
                            isSelected = isSelected,
                            onClick = {
                                onSelectIcon(iconItem.id)
                                onDismiss()
                            },
                        )
                    }
                }
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

@Composable
private fun IconGridCell(
    iconItem: SelectableIcon,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(10.dp)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(shape)
            .then(
                if (isSelected) {
                    Modifier
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), shape)
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f), shape)
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
    ) {
        Icon(
            painter = painterResource(id = iconItem.resId),
            contentDescription = iconItem.name,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(26.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = iconItem.name,
            fontFamily = InterFontFamily,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isSelected) 0.9f else 0.6f),
        )
    }
}
