package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Field
import com.example.model.Machine
import com.example.model.MachineStatus
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LiveFarmMapCanvas(
    fields: List<Field>,
    machines: List<Machine>,
    selectedMachineId: String?,
    onMachineSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    showYieldLayer: Boolean = false,
    showGuidanceLayer: Boolean = true,
    showTrailsLayer: Boolean = true
) {
    // Coordinate mapping bounding box
    val minLat = 41.865
    val maxLat = 41.895
    val minLng = -93.115
    val maxLng = -93.075

    var tappedMachineInfo by remember { mutableStateOf<Machine?>(null) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0D120D))
            .border(1.dp, CockpitBorder, RoundedCornerShape(12.dp))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(machines) {
                    detectTapGestures { tapOffset ->
                        val w = size.width.toFloat()
                        val h = size.height.toFloat()

                        // Find closest machine to tap
                        val hit = machines.minByOrNull { m ->
                            val x = ((m.longitude - minLng) / (maxLng - minLng) * w).toFloat()
                            val y = ((maxLat - m.latitude) / (maxLat - minLat) * h).toFloat()
                            val dx = x - tapOffset.x
                            val dy = y - tapOffset.y
                            (dx * dx + dy * dy)
                        }

                        if (hit != null) {
                            val x = ((hit.longitude - minLng) / (maxLng - minLng) * w).toFloat()
                            val y = ((maxLat - hit.latitude) / (maxLat - minLat) * h).toFloat()
                            val dist = kotlin.math.sqrt((x - tapOffset.x) * (x - tapOffset.x) + (y - tapOffset.y) * (y - tapOffset.y))
                            if (dist < 80) { // 80px touch radius
                                onMachineSelected(hit.id)
                                tappedMachineInfo = hit
                            }
                        }
                    }
                }
        ) {
            val canvasW = size.width
            val canvasH = size.height

            // 1. Draw Grid Lines & Background Road Polygons
            val gridStep = 80f
            for (x in 0..canvasW.toInt() step gridStep.toInt()) {
                drawLine(
                    color = Color(0xFF1B241B),
                    start = Offset(x.toFloat(), 0f),
                    end = Offset(x.toFloat(), canvasH),
                    strokeWidth = 1f
                )
            }
            for (y in 0..canvasH.toInt() step gridStep.toInt()) {
                drawLine(
                    color = Color(0xFF1B241B),
                    start = Offset(0f, y.toFloat()),
                    end = Offset(canvasW, y.toFloat()),
                    strokeWidth = 1f
                )
            }

            // 2. Draw Field Boundaries & Yield/Completed Swaths
            fields.forEach { field ->
                val points = field.boundaryPoints.map { pt ->
                    val x = ((pt.lng - minLng) / (maxLng - minLng) * canvasW).toFloat()
                    val y = ((maxLat - pt.lat) / (maxLat - minLat) * canvasH).toFloat()
                    Offset(x, y)
                }

                if (points.isNotEmpty()) {
                    val path = Path().apply {
                        moveTo(points[0].x, points[0].y)
                        for (i in 1 until points.size) {
                            lineTo(points[i].x, points[i].y)
                        }
                        close()
                    }

                    // Field Fill
                    val fillColor = if (showYieldLayer) {
                        MapYieldHigh.copy(alpha = 0.25f)
                    } else {
                        DeereGreen.copy(alpha = 0.20f)
                    }
                    drawPath(path, color = fillColor)

                    // Field Geofence Boundary Outline
                    drawPath(
                        path,
                        color = DeereYellow,
                        style = Stroke(
                            width = 2.5f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                        )
                    )

                    // Draw Guidance Lines if enabled
                    if (showGuidanceLayer) {
                        val firstPt = points[0]
                        val lastPt = points[2]
                        for (i in 1..8) {
                            val t = i / 9f
                            val lx1 = firstPt.x + (lastPt.x - firstPt.x) * t
                            val ly1 = firstPt.y
                            val ly2 = points[1].y
                            drawLine(
                                color = StatusWorkingGreen.copy(alpha = 0.35f),
                                start = Offset(lx1, ly1),
                                end = Offset(lx1, ly2),
                                strokeWidth = 1.5f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                            )
                        }
                    }
                }
            }

            // 3. Draw Completed Swaths & Moving Machine Paths
            if (showTrailsLayer) {
                machines.forEach { m ->
                    if (m.status == MachineStatus.WORKING || m.status == MachineStatus.HARVESTING) {
                        val mx = ((m.longitude - minLng) / (maxLng - minLng) * canvasW).toFloat()
                        val my = ((maxLat - m.latitude) / (maxLat - minLat) * canvasH).toFloat()

                        // Draw completed swath line behind tractor
                        val angleRad = Math.toRadians(m.headingDegrees.toDouble())
                        val backX = mx - (cos(angleRad) * 120).toFloat()
                        val backY = my - (sin(angleRad) * 120).toFloat()

                        drawLine(
                            color = StatusWorkingGreen.copy(alpha = 0.45f),
                            start = Offset(backX, backY),
                            end = Offset(mx, my),
                            strokeWidth = 18f
                        )
                    }
                }
            }

            // 4. Draw Machines & Autonomous Direction Vectors
            machines.forEach { machine ->
                val mx = ((machine.longitude - minLng) / (maxLng - minLng) * canvasW).toFloat()
                val my = ((maxLat - machine.latitude) / (maxLat - minLat) * canvasH).toFloat()
                val isSelected = machine.id == selectedMachineId

                val statusColor = when (machine.status) {
                    MachineStatus.WORKING -> StatusWorkingGreen
                    MachineStatus.HARVESTING -> StatusHarvestingYellow
                    MachineStatus.STANDBY -> StatusStandbyBlue
                    MachineStatus.MAINTENANCE -> StatusMaintenanceOrange
                    MachineStatus.TRANSIT -> DeereYellow
                    MachineStatus.ERROR -> StatusErrorRed
                }

                // Selected Machine Highlight Halo Ring
                if (isSelected) {
                    drawCircle(
                        color = DeereYellow.copy(alpha = 0.3f),
                        radius = 28f,
                        center = Offset(mx, my)
                    )
                    drawCircle(
                        color = DeereYellow,
                        radius = 28f,
                        center = Offset(mx, my),
                        style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f))
                    )
                }

                // Machine Marker Base Circle
                drawCircle(
                    color = CockpitBackground,
                    radius = 16f,
                    center = Offset(mx, my)
                )
                drawCircle(
                    color = statusColor,
                    radius = 14f,
                    center = Offset(mx, my)
                )

                // Direction Heading Arrow
                rotate(degrees = machine.headingDegrees, pivot = Offset(mx, my)) {
                    val arrowPath = Path().apply {
                        moveTo(mx, my - 12f)
                        lineTo(mx - 6f, my + 6f)
                        lineTo(mx, my + 2f)
                        lineTo(mx + 6f, my + 6f)
                        close()
                    }
                    drawPath(arrowPath, color = Color.Black)
                }

                // Autonomous Halo Marker
                if (machine.isAutonomous) {
                    drawCircle(
                        color = StatusWorkingGreen,
                        radius = 20f,
                        center = Offset(mx, my),
                        style = Stroke(width = 1.5f)
                    )
                }
            }
        }

        // Overlay Legend / Map Controls Top Right
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(CockpitSurface.copy(alpha = 0.9f))
                .border(1.dp, CockpitBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(StatusWorkingGreen))
                Text("WORKING", color = DeereTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(StatusHarvestingYellow))
                Text("HARVESTING", color = DeereTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(StatusStandbyBlue))
                Text("STANDBY", color = DeereTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(DeereYellow))
                Text("AUTONOMOUS", color = DeereTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Bottom Left Active Machine Map Tooltip
        selectedMachineId?.let { id ->
            machines.find { it.id == id }?.let { active ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .widthIn(max = 320.dp),
                    colors = CardDefaults.cardColors(containerColor = CockpitSurface.copy(alpha = 0.95f)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DeereYellow))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(active.name, color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DeereGreen)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(active.status.name, color = DeereYellow, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text("FIELD: ${active.currentFieldName} • ${active.currentJobTitle}", color = DeereTextSecondary, fontSize = 11.sp)

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("SPEED: ${active.speedKmH} km/h", color = StatusWorkingGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("FUEL: ${active.fuelPercent.toInt()}%", color = DeereYellow, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("ACRES: ${active.acresCompletedToday}", color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
