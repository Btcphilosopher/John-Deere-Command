package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.model.Field
import com.example.model.Machine
import com.example.model.MachineStatus
import com.example.ui.components.LiveFarmMapCanvas
import com.example.ui.theme.*

@Composable
fun TabletCommandCenterScreen(
    machines: List<Machine>,
    fields: List<Field>,
    selectedMachineId: String?,
    onSelectMachine: (String) -> Unit,
    onNavigateToTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalAcresToday = machines.sumOf { it.acresCompletedToday.toDouble() }.toFloat()
    val totalFuelUsedL = (machines.sumOf { (100 - it.fuelPercent).toDouble() * 12 }).toFloat()
    val activeMachineCount = machines.count { it.status == MachineStatus.WORKING || it.status == MachineStatus.HARVESTING }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left Column: Live Farm Operations Map (65% width)
        Column(
            modifier = Modifier
                .weight(0.65f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Live Farm Map Card Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CockpitSurface,
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(StatusWorkingGreen)
                        )
                        Text(
                            text = "LIVE FARM OPERATIONS MAP",
                            color = DeereTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onNavigateToTab(1) }, // Go to Map tab
                            colors = ButtonDefaults.buttonColors(containerColor = DeereGreen),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.OpenInFull, contentDescription = "Expand", modifier = Modifier.size(14.dp), tint = DeereYellow)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("EXPAND MAP", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Canvas Map Box
            LiveFarmMapCanvas(
                fields = fields,
                machines = machines,
                selectedMachineId = selectedMachineId,
                onMachineSelected = onSelectMachine,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Today Operations Metrics Ribbon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard("ACRES COMPLETED", "${totalAcresToday.toInt()}", "ACRES", StatusWorkingGreen, Modifier.weight(1f))
                MetricCard("EST. FUEL CONSUMPTION", "${totalFuelUsedL.toInt()} L", "DIESEL", DeereYellow, Modifier.weight(1f))
                MetricCard("FLEET PRODUCTIVITY", "+8.4%", "VS TARGET", StatusWorkingGreen, Modifier.weight(1f))
                MetricCard("TOTAL MACHINE HOURS", "36.2 hrs", "TODAY", StatusStandbyBlue, Modifier.weight(1f))
            }
        }

        // Right Column: Active Machines & Quick Controls (35% width)
        Column(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CockpitSurface,
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ACTIVE MACHINES ($activeMachineCount WORKING)",
                        color = DeereTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                    TextButton(onClick = { onNavigateToTab(3) }) {
                        Text("VIEW ALL 14", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Active Machine Quick List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(machines.take(6)) { machine ->
                    val isSelected = machine.id == selectedMachineId

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectMachine(machine.id)
                                onNavigateToTab(2) // Jump to Machine Cockpit
                            },
                        color = if (isSelected) CockpitSurfaceVariant else CockpitSurface,
                        shape = RoundedCornerShape(10.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) DeereYellow else CockpitBorder)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(machine.modelNumber, color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                when (machine.status) {
                                                    MachineStatus.WORKING -> StatusWorkingGreen.copy(alpha = 0.2f)
                                                    MachineStatus.HARVESTING -> StatusHarvestingYellow.copy(alpha = 0.2f)
                                                    else -> StatusStandbyBlue.copy(alpha = 0.2f)
                                                }
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            machine.status.name,
                                            color = when (machine.status) {
                                                MachineStatus.WORKING -> StatusWorkingGreen
                                                MachineStatus.HARVESTING -> StatusHarvestingYellow
                                                else -> StatusStandbyBlue
                                            },
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))
                                Text(machine.currentJobTitle, color = DeereTextSecondary, fontSize = 11.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("${machine.fuelPercent.toInt()}% FUEL", color = DeereYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("${machine.speedKmH} km/h", color = StatusWorkingGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Dispatch & Autonomous Quick Actions
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CockpitCard,
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("QUARK DISPATCH & COCKPIT", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onNavigateToTab(2) },
                            colors = ButtonDefaults.buttonColors(containerColor = DeereGreen),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("OPEN COCKPIT", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onNavigateToTab(6) },
                            colors = ButtonDefaults.buttonColors(containerColor = CockpitSurfaceVariant),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("AUTONOMOUS", color = DeereTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, mainVal: String, subVal: String, valColor: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = CockpitSurface,
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, color = DeereTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(mainVal, color = valColor, fontSize = 16.sp, fontWeight = FontWeight.Black)
            Text(subVal, color = DeereTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}
