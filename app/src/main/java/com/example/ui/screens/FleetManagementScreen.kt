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
import com.example.model.Machine
import com.example.model.MachineCategory
import com.example.model.MachineStatus
import com.example.ui.theme.*

@Composable
fun FleetManagementScreen(
    machines: List<Machine>,
    selectedCategory: MachineCategory?,
    onCategoryFilterChange: (MachineCategory?) -> Unit,
    selectedStatus: MachineStatus?,
    onStatusFilterChange: (MachineStatus?) -> Unit,
    onSelectMachine: (String) -> Unit,
    onNavigateToCockpit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = machines.filter { m ->
        (selectedCategory == null || m.category == selectedCategory) &&
                (selectedStatus == null || m.status == selectedStatus) &&
                (searchQuery.isBlank() || m.name.contains(searchQuery, ignoreCase = true) || m.operatorName.contains(searchQuery, ignoreCase = true) || m.currentFieldName.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Fleet Category Stats Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CategoryStatCard("TOTAL MACHINES", "${machines.size}", "14 CONNECTED", DeereYellow, Modifier.weight(1f)) { onCategoryFilterChange(null) }
            CategoryStatCard("TRACTORS", "${machines.count { it.category == MachineCategory.TRACTOR }}", "8 UNITS", StatusWorkingGreen, Modifier.weight(1f)) { onCategoryFilterChange(MachineCategory.TRACTOR) }
            CategoryStatCard("HARVESTING", "${machines.count { it.category == MachineCategory.COMBINE_HARVESTER }}", "3 COMBINES", StatusHarvestingYellow, Modifier.weight(1f)) { onCategoryFilterChange(MachineCategory.COMBINE_HARVESTER) }
            CategoryStatCard("SPRAYERS", "${machines.count { it.category == MachineCategory.SPRAYER }}", "2 BOOMS", StatusStandbyBlue, Modifier.weight(1f)) { onCategoryFilterChange(MachineCategory.SPRAYER) }
            CategoryStatCard("AUTONOMOUS", "${machines.count { it.isAutonomous }}", "3 UNITS", DeereGreenBright, Modifier.weight(1f)) { onCategoryFilterChange(MachineCategory.AUTONOMOUS_UNIT) }
        }

        // Filter Bar & Search Input
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CockpitSurface,
            shape = RoundedCornerShape(10.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by machine name, operator, field...", color = DeereTextMuted, fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = DeereTextSecondary) },
                    modifier = Modifier.width(320.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeereYellow,
                        unfocusedBorderColor = CockpitBorder,
                        focusedContainerColor = CockpitCard,
                        unfocusedContainerColor = CockpitCard,
                        focusedTextColor = DeereTextPrimary,
                        unfocusedTextColor = DeereTextPrimary
                    ),
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = {
                        onCategoryFilterChange(null)
                        onStatusFilterChange(null)
                        searchQuery = ""
                    }) {
                        Text("CLEAR FILTERS", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Machines List Table / Grid
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredList) { m ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelectMachine(m.id)
                            onNavigateToCockpit()
                        },
                    color = CockpitSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Machine Name & Details
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CockpitCard)
                                    .border(1.dp, CockpitBorder, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (m.isAutonomous) Icons.Default.PrecisionManufacturing else Icons.Default.Agriculture,
                                    contentDescription = "Icon",
                                    tint = if (m.isAutonomous) StatusWorkingGreen else DeereYellow,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(m.name, color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    if (m.isAutonomous) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(DeereGreen)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("AUTONOMOUS", color = DeereYellow, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("FIELD: ${m.currentFieldName} • OP: ${m.operatorName} • IMP: ${m.implementAttached}", color = DeereTextSecondary, fontSize = 11.sp)
                            }
                        }

                        // Right: Status, Fuel, Speed, Hours
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text("STATUS", color = DeereTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(m.status.name, color = when(m.status) {
                                    MachineStatus.WORKING -> StatusWorkingGreen
                                    MachineStatus.HARVESTING -> StatusHarvestingYellow
                                    MachineStatus.STANDBY -> StatusStandbyBlue
                                    else -> StatusMaintenanceOrange
                                }, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("FUEL", color = DeereTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text("${m.fuelPercent.toInt()}%", color = DeereYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("SPEED", color = DeereTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text("${m.speedKmH} km/h", color = StatusWorkingGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("HOURS", color = DeereTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text("${m.totalMachineHours.toInt()} hrs", color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Icon(Icons.Default.ChevronRight, contentDescription = "Details", tint = DeereTextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryStatCard(title: String, mainVal: String, subVal: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = CockpitSurface,
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, color = DeereTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(mainVal, color = color, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Text(subVal, color = DeereTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}
