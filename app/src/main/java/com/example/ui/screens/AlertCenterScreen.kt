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
import com.example.model.AlertSeverity
import com.example.ui.theme.*

@Composable
fun AlertCenterScreen(
    alerts: List<AlertItem>,
    onAcknowledgeAlert: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
                    Text("ALERT & DTC DIAGNOSTIC CENTER", color = DeereYellow, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("TOTAL ALERTS: ${alerts.size} (${alerts.count { !it.isAcknowledged }} UNACKNOWLEDGED)", color = DeereTextSecondary, fontSize = 11.sp)
                }
            }
        }

        // Alerts List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(alerts) { alert ->
                val severityColor = when (alert.severity) {
                    AlertSeverity.CRITICAL -> StatusErrorRed
                    AlertSeverity.WARNING -> StatusWarningYellow
                    AlertSeverity.INFO -> StatusStandbyBlue
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CockpitSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (!alert.isAcknowledged) severityColor else CockpitBorder))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(severityColor.copy(alpha = 0.2f))
                                    .border(1.dp, severityColor, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (alert.severity) {
                                        AlertSeverity.CRITICAL -> Icons.Default.Error
                                        AlertSeverity.WARNING -> Icons.Default.Warning
                                        AlertSeverity.INFO -> Icons.Default.Info
                                    },
                                    contentDescription = "Alert",
                                    tint = severityColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(alert.title, color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    alert.dtcCode?.let { code ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(CockpitCard)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(code, color = DeereYellow, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${alert.machineName} • ${alert.timestamp} — ${alert.description}", color = DeereTextSecondary, fontSize = 11.sp)
                            }
                        }

                        if (!alert.isAcknowledged) {
                            Button(
                                onClick = { onAcknowledgeAlert(alert.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = severityColor),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("ACKNOWLEDGE", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text("ACKNOWLEDGED", color = DeereTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
