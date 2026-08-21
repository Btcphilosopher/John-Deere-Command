package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HotspotComponent
import com.example.model.Machine
import com.example.ui.theme.*

@Composable
fun HotspotInspector3D(
    machine: Machine,
    hotspots: List<HotspotComponent>,
    modifier: Modifier = Modifier
) {
    var selectedHotspot by remember { mutableStateOf<HotspotComponent?>(hotspots.firstOrNull()) }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CockpitSurface)
            .border(1.dp, CockpitBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "3D CHASSIS DIAGNOSTIC HOTSPOT INSPECTOR",
                    color = DeereYellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${machine.name} • SUBSYSTEM HEALTH CHECK",
                    color = DeereTextSecondary,
                    fontSize = 11.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(StatusWorkingGreen.copy(alpha = 0.2f))
                    .border(1.dp, StatusWorkingGreen, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "ISOBUS CLASS 3 LIVE",
                    color = StatusWorkingGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tractor Vector Schematic Drawing Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(CockpitBackground)
                .border(1.dp, CockpitBorder, RoundedCornerShape(8.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Draw Grid lines
                for (i in 0..10) {
                    val x = w * (i / 10f)
                    drawLine(Color(0xFF1B241B), Offset(x, 0f), Offset(x, h), strokeWidth = 1f)
                }
                for (i in 0..6) {
                    val y = h * (i / 6f)
                    drawLine(Color(0xFF1B241B), Offset(0f, y), Offset(w, y), strokeWidth = 1f)
                }

                // Draw Heavy 8R Tractor Chassis Silhouette
                val chassisPath = Path().apply {
                    // Front Engine Hood
                    moveTo(w * 0.15f, h * 0.55f)
                    lineTo(w * 0.40f, h * 0.55f)
                    // Cab Windshield & Roof
                    lineTo(w * 0.45f, h * 0.25f)
                    lineTo(w * 0.65f, h * 0.25f)
                    // Rear Cab & Fender
                    lineTo(w * 0.70f, h * 0.60f)
                    // Hitch / Implement Joint
                    lineTo(w * 0.88f, h * 0.60f)
                    lineTo(w * 0.88f, h * 0.75f)
                    lineTo(w * 0.15f, h * 0.75f)
                    close()
                }

                // Draw Chassis Fill & Outline
                drawPath(chassisPath, color = DeereGreenDark.copy(alpha = 0.4f))
                drawPath(chassisPath, color = DeereGreenBright, style = Stroke(width = 2.5f))

                // Draw Wheels (Front & Dual Rear Wheels)
                // Front Wheel
                drawCircle(color = Color(0xFF2B362B), radius = 28f, center = Offset(w * 0.28f, h * 0.75f))
                drawCircle(color = DeereYellow, radius = 28f, center = Offset(w * 0.28f, h * 0.75f), style = Stroke(width = 3f))

                // Rear Dual Wheel
                drawCircle(color = Color(0xFF2B362B), radius = 42f, center = Offset(w * 0.62f, h * 0.75f))
                drawCircle(color = DeereYellow, radius = 42f, center = Offset(w * 0.62f, h * 0.75f), style = Stroke(width = 4f))

                // Draw Hotspot Connecting Lines
                hotspots.forEach { hs ->
                    val hx = w * hs.xRatio
                    val hy = h * hs.yRatio
                    val isSelected = hs.id == selectedHotspot?.id

                    val dotColor = if (hs.isWarning) StatusWarningYellow else if (isSelected) DeereYellow else StatusWorkingGreen

                    // Connect Line to Hotspot Node
                    drawLine(
                        color = dotColor.copy(alpha = 0.6f),
                        start = Offset(hx, hy),
                        end = Offset(hx, hy - 25f),
                        strokeWidth = 1.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                    )

                    drawCircle(color = dotColor.copy(alpha = 0.3f), radius = 16f, center = Offset(hx, hy))
                    drawCircle(color = dotColor, radius = 8f, center = Offset(hx, hy))
                    if (isSelected) {
                        drawCircle(color = DeereYellow, radius = 20f, center = Offset(hx, hy), style = Stroke(width = 2f))
                    }
                }
            }

            // Clickable Hotspot Overlay Buttons
            hotspots.forEach { hs ->
                val isSelected = hs.id == selectedHotspot?.id

                Box(
                    modifier = Modifier
                        .offset(
                            x = (280 * hs.xRatio).dp,
                            y = (180 * hs.yRatio).dp
                        )
                        .clip(CircleShape)
                        .clickable { selectedHotspot = hs }
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Hotspot Subsystem Details Panel
        selectedHotspot?.let { hs ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CockpitCard,
                shape = RoundedCornerShape(8.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (hs.isWarning) StatusWarningYellow else CockpitBorder))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = if (hs.isWarning) Icons.Default.Warning else Icons.Default.Build,
                                contentDescription = "Subsystem",
                                tint = if (hs.isWarning) StatusWarningYellow else StatusWorkingGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(hs.name, color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Category: ${hs.category} • ${hs.statusText}", color = DeereTextSecondary, fontSize = 11.sp)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("HEALTH", color = DeereTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text("${hs.healthPercent}%", color = if (hs.healthPercent > 90) StatusWorkingGreen else StatusWarningYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        hs.tempC?.let { temp ->
                            Column(horizontalAlignment = Alignment.End) {
                                Text("TEMP", color = DeereTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text("${temp.toInt()}°C", color = if (temp > 80) StatusErrorRed else DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                        hs.pressurePsi?.let { press ->
                            Column(horizontalAlignment = Alignment.End) {
                                Text("PRESSURE", color = DeereTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text("${press.toInt()} PSI", color = DeereYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
