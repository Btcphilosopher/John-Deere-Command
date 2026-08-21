package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun JohnDeereCircularGauge(
    title: String,
    value: Float,
    minValue: Float,
    maxValue: Float,
    unit: String,
    activeColor: Color = StatusWorkingGreen,
    modifier: Modifier = Modifier
) {
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = tween(durationMillis = 800),
        label = "gaugeAnimation"
    )

    val sweepAngleTotal = 240f
    val startAngle = 150f
    val currentSweep = ((animatedValue - minValue) / (maxValue - minValue) * sweepAngleTotal).coerceIn(0f, sweepAngleTotal)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CockpitSurface)
            .border(1.dp, CockpitBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val diameter = size.minDimension
                    val strokeWidth = 12f
                    val arcSize = Size(diameter - strokeWidth, diameter - strokeWidth)
                    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                    // Track Arc
                    drawArc(
                        color = CockpitCard,
                        startAngle = startAngle,
                        sweepAngle = sweepAngleTotal,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Value Arc
                    drawArc(
                        color = activeColor,
                        startAngle = startAngle,
                        sweepAngle = currentSweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (animatedValue >= 100) "${animatedValue.toInt()}" else "%.1f".format(animatedValue),
                        color = DeereTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = unit,
                        color = DeereTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title.uppercase(),
                color = DeereTextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun JohnDeereLinearMeter(
    title: String,
    valuePercent: Float, // 0 to 100
    displayValueText: String,
    activeColor: Color = StatusWorkingGreen,
    modifier: Modifier = Modifier
) {
    val animatedPercent by animateFloatAsState(
        targetValue = valuePercent,
        animationSpec = tween(durationMillis = 600),
        label = "linearMeterAnimation"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CockpitSurface)
            .border(1.dp, CockpitBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title.uppercase(), color = DeereTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(displayValueText, color = DeereTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Black)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(CockpitCard)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = (animatedPercent / 100f).coerceIn(0f, 1f))
                    .clip(RoundedCornerShape(5.dp))
                    .background(activeColor)
            )
        }
    }
}
