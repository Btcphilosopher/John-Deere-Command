package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MaintenanceItem
import com.example.model.ServiceStatus
import com.example.ui.theme.*

@Composable
fun MaintenanceDiagnosticsScreen(
    maintenanceItems: List<MaintenanceItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CockpitSurface,
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ENTERPRISE FLEET MAINTENANCE & SERVICE RECORDS", color = DeereYellow, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("PREVENTIVE MAINTENANCE SCHEDULER & PARTS INVENTORY", color = DeereTextSecondary, fontSize = 11.sp)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(maintenanceItems) { item ->
                val statusColor = when (item.status) {
                    ServiceStatus.OK -> StatusWorkingGreen
                    ServiceStatus.DUE_SOON -> StatusWarningYellow
                    ServiceStatus.OVERDUE -> StatusErrorRed
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
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
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(statusColor.copy(alpha = 0.2f))
                                    .border(1.dp, statusColor, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (item.status == ServiceStatus.OK) Icons.Default.CheckCircle else Icons.Default.Build,
                                    contentDescription = "Service",
                                    tint = statusColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Text("${item.machineName} • ${item.componentName}", color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("TECH: ${item.assignedTechnician} • LAST SERVICE: ${item.lastServicedDate}", color = DeereTextSecondary, fontSize = 11.sp)
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("${item.remainingHours.toInt()} HOURS REMAINING", color = statusColor, fontWeight = FontWeight.Black, fontSize = 13.sp)
                            Text("INTERVAL: ${item.intervalHours.toInt()} HRS", color = DeereTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
