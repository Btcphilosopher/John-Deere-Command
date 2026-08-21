package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AnalyticsEconomicsScreen(modifier: Modifier = Modifier) {
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
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ENTERPRISE FARM ECONOMICS & FLEET ANALYTICS", color = DeereYellow, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text("OPERATING COST PER ACRE & MACHINE EFFICIENCY COMPARATOR", color = DeereTextSecondary, fontSize = 11.sp)
            }
        }

        // Metrics Summary Row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            EconMetricCard("AVG OPERATING COST", "$14.20", "PER ACRE", StatusWorkingGreen, Modifier.weight(1f))
            EconMetricCard("DIESEL FUEL COST", "$1,840.00", "TODAY", DeereYellow, Modifier.weight(1f))
            EconMetricCard("FLEET UTILIZATION", "88.4%", "12.2 OPERATIONAL HRS", StatusWorkingGreen, Modifier.weight(1f))
            EconMetricCard("PREVENTABLE DOWNTIME", "0.4 hrs", "LOWEST THIS MONTH", StatusWorkingGreen, Modifier.weight(1f))
        }

        // Machine Comparison Tool: Machine A vs Machine B
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CockpitSurface,
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("MACHINE EFFICIENCY COMPARISON TOOL", color = DeereYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Machine A
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = CockpitCard,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("MACHINE A: JOHN DEERE 8R 410", color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("FUEL EFFICIENCY: 4.2 L / Acre", color = StatusWorkingGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("PRODUCTIVITY: 14.2 Acres / Hr", color = DeereTextSecondary, fontSize = 11.sp)
                            Text("COST / ACRE: $13.80 / Ac", color = DeereYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    // Machine B
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = CockpitCard,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("MACHINE B: 8R 370 AUTONOMOUS", color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("FUEL EFFICIENCY: 3.8 L / Acre (-9.5%)", color = StatusWorkingGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("PRODUCTIVITY: 12.8 Acres / Hr", color = DeereTextSecondary, fontSize = 11.sp)
                            Text("COST / ACRE: $11.40 / Ac (-17.3%)", color = StatusWorkingGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EconMetricCard(title: String, mainVal: String, subVal: String, valColor: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = CockpitSurface,
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, color = DeereTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(mainVal, color = valColor, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Text(subVal, color = DeereTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}
