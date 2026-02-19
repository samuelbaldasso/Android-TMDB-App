package com.sbaldasso.tmdbapp.presentation.screen.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val SplashBackground = Color(0xFF0D1B2A)
private val SplashPrimary = Color(0xFF1976D2)
private val SplashAccent = Color(0xFF03DAC6)
private val SplashWhite = Color(0xFFFFFFFF)
private val SplashDark = Color(0xFF0D1B2A)

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.75f) }

    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600, easing = EaseOutCubic)
            )
        }
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600, easing = EaseOutCubic)
            )
        }
        delay(2_000L)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .alpha(alpha.value)
                .scale(scale.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Canvas(modifier = Modifier.size(120.dp)) {
                drawClapperboard()
            }

            Text(
                text = "TMDB App",
                color = SplashWhite,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Text(
                text = "Your Movie Universe",
                color = SplashAccent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                letterSpacing = 1.sp
            )
        }
    }
}

private fun DrawScope.drawClapperboard() {
    val w = size.width
    val h = size.height

    // Clapperboard body (teal)
    drawRoundRect(
        color = SplashAccent,
        topLeft = Offset(w * 0.05f, h * 0.40f),
        size = Size(w * 0.90f, h * 0.52f),
        cornerRadius = CornerRadius(12f, 12f)
    )

    // Top bar (primary blue)
    drawRoundRect(
        color = SplashPrimary,
        topLeft = Offset(w * 0.05f, h * 0.24f),
        size = Size(w * 0.90f, h * 0.18f),
        cornerRadius = CornerRadius(8f, 8f)
    )

    // Diagonal stripes on top bar
    val barLeft = w * 0.05f
    val barWidth = w * 0.90f
    val barTop = h * 0.24f
    val barBottom = h * 0.42f
    val stripeWidth = barWidth / 8f
    for (i in 0 until 4) {
        val startX = barLeft + i * stripeWidth * 2f
        val path = Path().apply {
            moveTo(startX, barTop)
            lineTo(startX + stripeWidth, barTop)
            lineTo(startX, barBottom)
            close()
        }
        drawPath(path, color = SplashWhite)
    }

    // Left film reel (dark circle)
    drawCircle(
        color = SplashDark,
        radius = w * 0.16f,
        center = Offset(w * 0.30f, h * 0.67f)
    )
    // Left reel center dot (accent)
    drawCircle(
        color = SplashAccent,
        radius = w * 0.06f,
        center = Offset(w * 0.30f, h * 0.67f)
    )

    // Right film reel (dark circle)
    drawCircle(
        color = SplashDark,
        radius = w * 0.16f,
        center = Offset(w * 0.70f, h * 0.67f)
    )
    // Right reel center dot (accent)
    drawCircle(
        color = SplashAccent,
        radius = w * 0.06f,
        center = Offset(w * 0.70f, h * 0.67f)
    )

    // Film strip band connecting reels
    drawRect(
        color = SplashDark,
        topLeft = Offset(w * 0.30f, h * 0.62f),
        size = Size(w * 0.40f, h * 0.10f)
    )
}
