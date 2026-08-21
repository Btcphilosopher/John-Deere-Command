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
import com.example.model.AlertItem
import com.example.model.Machine
import com.example.model.MachineStatus
import com.example.ui.theme.*

@Composable
fun MobileCompanionScreen(
    machines: List<Machine>,
    alerts: List<AlertItem>,
    onSelectMachine: (String) -> Unit,
    onNavigateToTablet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Mobile Companion Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CockpitSurface,
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DeereYellow))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Smartphone, contentDescription = "Mobile", tint = DeereYellow)
                    Column {
                        Text("JD COMMAND MOBILE", color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("COMMAND COMPANION", color = DeereTextSecondary, fontSize = 10.sp)
                    }
                }

                Button(
                    onClick = onNavigateToTablet,
                    colors = ButtonDefaults.buttonColors(containerColor = DeereGreen),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("SWITCH TO TABLET", color = DeereYellow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Fleet Quick Snapshot
        Text("FLEET STATUS (${machines.size} UNITS)", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(machines) { m ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CockpitSurface,
                    shape = RoundedCornerShape(8.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(m.name, color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${m.currentFieldName} • ${m.speedKmH} km/h", color = DeereTextSecondary, fontSize = 11.sp)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(m.status.name, color = when(m.status) {
                                MachineStatus.WORKING -> StatusWorkingGreen
                                MachineStatus.HARVESTING -> StatusHarvestingYellow
                                else -> StatusStandbyBlue
                            }, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("${m.fuelPercent.toInt()}% FUEL", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
