package com.capycdr786.minimalistlauncher.ui.util

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

fun Modifier.onSwipeUp(enabled: Boolean = true, onSwipe: () -> Unit): Modifier =
    if (!enabled) this
    else pointerInput(enabled) {
        awaitEachGesture {
            val down = awaitFirstDown(pass = PointerEventPass.Initial)
            var totalDragY = 0f
            var totalDragX = 0f

            while (true) {
                val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                if (!change.pressed) break

                val deltaY = change.position.y - change.previousPosition.y
                val deltaX = change.position.x - change.previousPosition.x
                totalDragY += deltaY
                totalDragX += deltaX

                if (totalDragY < -18f && (abs(totalDragY) > abs(totalDragX))) {
                    onSwipe()
                    change.consume()
                    break
                }
            }
        }
    }

fun Modifier.onSwipeDown(enabled: Boolean = true, onSwipe: () -> Unit): Modifier =
    if (!enabled) this
    else pointerInput(enabled) {
        awaitEachGesture {
            val down = awaitFirstDown(pass = PointerEventPass.Initial)
            var totalDragY = 0f
            var totalDragX = 0f

            while (true) {
                val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                if (!change.pressed) break

                val deltaY = change.position.y - change.previousPosition.y
                val deltaX = change.position.x - change.previousPosition.x
                totalDragY += deltaY
                totalDragX += deltaX

                if (totalDragY > 18f && (abs(totalDragY) > abs(totalDragX))) {
                    onSwipe()
                    change.consume()
                    break
                }
            }
        }
    }
