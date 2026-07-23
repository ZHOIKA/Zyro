package com.my.zyro.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Particle(
    var x: Float,
    var y: Float,
    val baseSpeedX: Float,
    val baseSpeedY: Float,
    val amplitude: Float,
    val frequency: Float,
    val phase: Float,
    val radius: Float,
    val alpha: Float,
    val alphaSpeed: Float,
    val colorIndex: Int,
)

@Composable
fun ParticleEffect(
    modifier: Modifier = Modifier,
    particleCount: Int = 35,
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val colors = remember(primary, secondary) {
        listOf(primary, secondary)
    }

    var particles by remember { mutableStateOf<List<Particle>>(emptyList()) }

    LaunchedEffect(Unit) {
        particles = List(particleCount) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                baseSpeedX = (Random.nextFloat() - 0.5f) * 0.03f,
                baseSpeedY = (Random.nextFloat() - 0.5f) * 0.03f,
                amplitude = Random.nextFloat() * 0.02f + 0.005f,
                frequency = Random.nextFloat() * 0.5f + 0.2f,
                phase = Random.nextFloat() * 6.28f,
                radius = Random.nextFloat() * 3f + 1.5f,
                alpha = Random.nextFloat() * 0.4f + 0.15f,
                alphaSpeed = Random.nextFloat() * 0.003f + 0.001f,
                colorIndex = Random.nextInt(colors.size),
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "particleAlpha")
    val alphaPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "alphaPhase",
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        if (particles.isEmpty()) return@Canvas

        val w = size.width
        val h = size.height

        for (p in particles) {
            val waveX = sin(alphaPhase * p.frequency + p.phase) * p.amplitude * w
            val waveY = cos(alphaPhase * p.frequency * 0.7f + p.phase) * p.amplitude * h

            p.x += p.baseSpeedX + waveX * 0.0001f
            p.y += p.baseSpeedY + waveY * 0.0001f

            if (p.x < 0f) p.x = 1f
            if (p.x > 1f) p.x = 0f
            if (p.y < 0f) p.y = 1f
            if (p.y > 1f) p.y = 0f

            val currentAlpha = (sin(alphaPhase * p.alphaSpeed * 10f + p.phase) * 0.5f + 0.5f) * p.alpha
            val color = colors[p.colorIndex % colors.size].copy(alpha = currentAlpha)

            val cx = p.x * w
            val cy = p.y * h

            drawCircle(color = color.copy(alpha = currentAlpha * 0.3f), radius = p.radius * 3f, center = Offset(cx, cy))
            drawCircle(color = color.copy(alpha = currentAlpha * 0.6f), radius = p.radius * 1.5f, center = Offset(cx, cy))
            drawCircle(color = color, radius = p.radius, center = Offset(cx, cy))
        }
    }
}
