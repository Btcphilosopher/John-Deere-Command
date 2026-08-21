package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.Machine
import com.example.model.MachineStatus
import com.example.ui.theme.*

@Composable
fun AutonomousSupervisionScreen(
    machines: List<Machine>,
    onSelectMachine: (String) -> Unit,
    onPauseMachine: () -> Unit,
    onEmergencySafeState: () -> Unit,
    modifier: Modifier = Modifier
) {
    val autoUnits = machines.filter { it.isAutonomous }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
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
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.PrecisionManufacturing, contentDescription = "Auto", tint = StatusWorkingGreen, modifier = Modifier.size(24.dp))
                    Column {
                        Text("AUTONOMOUS & CONNECTED SUPERVISION CENTRE", color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 1.sp)
                        Text("${autoUnits.size} ROBOTIC UNITS UNDER LIVE SUPERVISION", color = DeereTextSecondary, fontSize = 11.sp)
                    }
                }

                Button(
                    onClick = onEmergencySafeState,
                    colors = ButtonDefaults.buttonColors(containerColor = StatusErrorRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Emergency, contentDescription = "Emergency Stop", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("EMERGENCY ALL SAFE STATE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Autonomous Machine Units List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(autoUnits) { unit ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CockpitSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(unit.name, color = DeereTextPrimary, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(StatusWorkingGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(unit.status.name, color = StatusWorkingGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Text("GNSS RTK PRECISION: 1.2 cm", color = DeereYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        // Simulated Camera / Perception Feed Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F150F))
                                .border(1.dp, CockpitBorder, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Videocam, contentDescription = "Camera Feed", tint = StatusWorkingGreen, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("STEREO CAMERAS & LIDAR PERCEPTION FEED ACTIVE", color = DeereTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("OBSTACLE SCAN CLEAR • BOUNDARY GEOFENCE LOCKED", color = DeereTextMuted, fontSize = 9.sp)
                            }
                        }

                        // Action Control Buttons for Supervisor
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { onSelectMachine(unit.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = DeereGreen),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("INSPECT TELEMETRY", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = onPauseMachine,
                                colors = ButtonDefaults.buttonColors(containerColor = CockpitCard),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("PAUSE AUTONOMOUS ROUTE", color = DeereTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
