package com.capycdr786.minimalistlauncher.ui.util

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.abs

@Composable
fun rememberWheelFlingBehavior(
    frictionMultiplier: Float = 0.52f,
    velocityMultiplier: Float = 1.15f,
): FlingBehavior {
    val decaySpec = remember(frictionMultiplier) {
        exponentialDecay<Float>(
            frictionMultiplier = frictionMultiplier,
            absVelocityThreshold = 0.1f,
        )
    }
    return remember(decaySpec, velocityMultiplier) {
        object : FlingBehavior {
            override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                val boostedVelocity = initialVelocity * velocityMultiplier
                if (abs(boostedVelocity) > 1f) {
                    var velocityLeft = boostedVelocity
                    var lastValue = 0f
                    val animationState = AnimationState(
                        initialValue = 0f,
                        initialVelocity = boostedVelocity,
                    )
                    try {
                        animationState.animateDecay(decaySpec) {
                            val delta = value - lastValue
                            val consumed = scrollBy(delta)
                            lastValue = value
                            velocityLeft = this.velocity
                            if (abs(delta - consumed) > 0.5f) {
                                cancelAnimation()
                            }
                        }
                    } catch (_: CancellationException) {
                        velocityLeft = animationState.velocity
                    }
                    return velocityLeft
                }
                return initialVelocity
            }
        }
    }
}
