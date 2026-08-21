package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Router
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.JohnDeereLinearMeter
import com.example.ui.theme.*

@Composable
fun ConnectivityNetworkScreen(modifier: Modifier = Modifier) {
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
                    Text("ISOBUS CLASS 3 & JDLINK CLOUD NETWORK STATUS", color = DeereYellow, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("14 / 14 FLEET MACHINES FULLY CONNECTED", color = StatusWorkingGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(StatusWorkingGreen.copy(alpha = 0.2f))
                        .border(1.dp, StatusWorkingGreen, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("LATENCY: 18ms", color = StatusWorkingGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Bus Networks Grids
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            NetworkCard("CELLULAR 5G INDUSTRIAL PRIVATE BUS", "95% SIGNAL", "2.4 GB/s THROUGHPUT", Icons.Default.CellTower, StatusWorkingGreen)
            NetworkCard("GNSS RTK DUAL-ANTENNA POSITIONING", "98% LOCK", "1.2 cm SUB-INCH ACCURACY", Icons.Default.GpsFixed, StatusWorkingGreen)
            NetworkCard("ISOBUS CLASS 3 MACHINE CAN-BUS", "100% HEALTH", "TRACTOR-IMPLEMENT CONTROL ONLINE", Icons.Default.Router, DeereYellow)
            NetworkCard("JDLINK CLOUD FLEET SYNC ENGINE", "CONNECTED", "ZERO LOSS ENCRYPTED SYNC", Icons.Default.CloudDone, StatusWorkingGreen)
        }
    }
}

@Composable
fun NetworkCard(title: String, val1: String, val2: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CockpitSurface,
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(icon, contentDescription = "Icon", tint = color, modifier = Modifier.size(28.dp))
                Column {
                    Text(title, color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(val2, color = DeereTextSecondary, fontSize = 11.sp)
                }
            }

            Text(val1, color = color, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
    }
}
