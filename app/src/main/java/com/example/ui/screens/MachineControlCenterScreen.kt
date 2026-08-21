package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HotspotComponent
import com.example.model.Machine
import com.example.model.MachineStatus
import com.example.ui.components.HotspotInspector3D
import com.example.ui.components.JohnDeereCircularGauge
import com.example.ui.components.JohnDeereLinearMeter
import com.example.ui.theme.*

@Composable
fun MachineControlCenterScreen(
    machine: Machine?,
    hotspots: List<HotspotComponent>,
    cockpitSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    implementDepthCm: Int,
    onDepthChange: (Int) -> Unit,
    controlMode: String,
    onModeChange: (String) -> Unit,
    onStartOperation: () -> Unit,
    onPauseJob: () -> Unit,
    onResumeJob: () -> Unit,
    onStopOperation: () -> Unit,
    onEmergencySafeState: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (machine == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("NO MACHINE SELECTED", color = DeereTextSecondary)
        }
        return
    }

    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left Panel: Live Telemetry Gauges & 3D Hotspot Inspector (60% width)
        Column(
            modifier = Modifier
                .weight(0.60f)
                .fillMaxHeight()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Machine Name & Header Strip
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CockpitSurface,
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DeereYellow))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(machine.name, color = DeereTextPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(StatusWorkingGreen.copy(alpha = 0.2f))
                                    .border(1.dp, StatusWorkingGreen, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("CONNECTED", color = StatusWorkingGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "FIELD: ${machine.currentFieldName} • JOB: ${machine.currentJobTitle} • OP: ${machine.operatorName}",
                            color = DeereTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                when (machine.status) {
                                    MachineStatus.WORKING -> StatusWorkingGreen
                                    MachineStatus.HARVESTING -> StatusHarvestingYellow
                                    else -> StatusStandbyBlue
                                }
                            )
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = machine.status.name,
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Circular Gauges Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                JohnDeereCircularGauge("Engine RPM", machine.engineRpm.toFloat(), 0f, 2500f, "RPM", DeereYellow, Modifier.weight(1f))
                JohnDeereCircularGauge("Speed", machine.speedKmH, 0f, 30f, "km/h", StatusWorkingGreen, Modifier.weight(1f))
                JohnDeereCircularGauge("Engine Load", machine.engineLoadPercent, 0f, 100f, "%", GaugeNeedleRed, Modifier.weight(1f))
            }

            // Linear Meters Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                JohnDeereLinearMeter("Fuel Tank Level", machine.fuelPercent, "${machine.fuelPercent.toInt()}%", DeereYellow, Modifier.weight(1f))
                JohnDeereLinearMeter("Hydraulic Temp", (machine.hydraulicTempC / 120f) * 100f, "${machine.hydraulicTempC.toInt()}°C", if (machine.hydraulicTempC > 80) StatusErrorRed else StatusWorkingGreen, Modifier.weight(1f))
                JohnDeereLinearMeter("Productivity Rate", (machine.productivityAcresPerHr / 50f) * 100f, "%.1f Ac/Hr".format(machine.productivityAcresPerHr), StatusWorkingGreen, Modifier.weight(1f))
            }

            // 3D Chassis Hotspot Inspector Component
            HotspotInspector3D(machine = machine, hotspots = hotspots, modifier = Modifier.fillMaxWidth())
        }

        // Right Panel: Operational Controls Panel & Mode Selectors (40% width)
        Column(
            modifier = Modifier
                .weight(0.40f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Machine Status Controls Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CockpitSurface,
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("OPERATIONAL COMMAND CENTRE", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

                    // Primary State Buttons
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onStartOperation,
                            colors = ButtonDefaults.buttonColors(containerColor = DeereGreen),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("START / ACTIVE", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onPauseJob,
                            colors = ButtonDefaults.buttonColors(containerColor = CockpitCard),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("PAUSE JOB", color = DeereTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onResumeJob,
                            colors = ButtonDefaults.buttonColors(containerColor = CockpitCard),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("RESUME JOB", color = DeereTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onStopOperation,
                            colors = ButtonDefaults.buttonColors(containerColor = StatusErrorRed.copy(alpha = 0.8f)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("STOP OPERATION", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Sliders Panel (Target Speed & Implement Depth)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CockpitSurface,
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Speed Control
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("TARGET WORKING SPEED", color = DeereTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("%.1f km/h".format(cockpitSpeed), color = StatusWorkingGreen, fontSize = 12.sp, fontWeight = FontWeight.Black)
                    }
                    Slider(
                        value = cockpitSpeed,
                        onValueChange = onSpeedChange,
                        valueRange = 0f..25f,
                        colors = SliderDefaults.colors(thumbColor = DeereYellow, activeTrackColor = StatusWorkingGreen, inactiveTrackColor = CockpitCard)
                    )

                    // Implement Depth
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("IMPLEMENT WORKING DEPTH", color = DeereTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("$implementDepthCm cm", color = DeereYellow, fontSize = 12.sp, fontWeight = FontWeight.Black)
                    }
                    Slider(
                        value = implementDepthCm.toFloat(),
                        onValueChange = { onDepthChange(it.toInt()) },
                        valueRange = 0f..35f,
                        colors = SliderDefaults.colors(thumbColor = DeereYellow, activeTrackColor = DeereYellow, inactiveTrackColor = CockpitCard)
                    )
                }
            }

            // Guidance & Operation Mode Selector
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CockpitSurface,
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("CONTROL MODE & AUTOTRAC", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("AUTO", "ASSIST", "MANUAL").forEach { mode ->
                            val isSelected = controlMode == mode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) DeereGreen else CockpitCard)
                                    .border(1.dp, if (isSelected) DeereYellow else CockpitBorder, RoundedCornerShape(8.dp))
                                    .clickable { onModeChange(mode) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    mode,
                                    color = if (isSelected) DeereYellow else DeereTextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Safety Emergency Return to Safe State Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEmergencySafeState() },
                color = StatusErrorRed.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StatusErrorRed))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Emergency, contentDescription = "Emergency", tint = StatusErrorRed, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("RETURN MACHINE TO SAFE STATE", color = StatusErrorRed, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
                }
            }
        }
    }
}
