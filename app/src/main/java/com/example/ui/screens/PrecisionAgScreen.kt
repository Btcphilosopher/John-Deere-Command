package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.ui.components.LiveFarmMapCanvas
import com.example.ui.theme.*

@Composable
fun PrecisionAgScreen(
    fields: List<Field>,
    machines: List<Machine>,
    selectedMachineId: String?,
    onSelectMachine: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showYieldLayer by remember { mutableStateOf(true) }
    var showSoilLayer by remember { mutableStateOf(false) }
    var showApplicationLayer by remember { mutableStateOf(true) }
    var showPathLayer by remember { mutableStateOf(true) }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Map Container
        Box(
            modifier = Modifier
                .weight(0.70f)
                .fillMaxHeight()
        ) {
            LiveFarmMapCanvas(
                fields = fields,
                machines = machines,
                selectedMachineId = selectedMachineId,
                onMachineSelected = onSelectMachine,
                modifier = Modifier.fillMaxSize(),
                showYieldLayer = showYieldLayer,
                showGuidanceLayer = showApplicationLayer,
                showTrailsLayer = showPathLayer
            )
        }

        // Layer Controls & Field Precision Stats (30% width)
        Column(
            modifier = Modifier
                .weight(0.30f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CockpitSurface,
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("PRECISION MAP LAYERS", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

                    LayerToggleRow("Yield Contour Map", showYieldLayer) { showYieldLayer = !showYieldLayer }
                    LayerToggleRow("Soil Moisture & pH Layer", showSoilLayer) { showSoilLayer = !showSoilLayer }
                    LayerToggleRow("Application Rate Overlay", showApplicationLayer) { showApplicationLayer = !showApplicationLayer }
                    LayerToggleRow("Machine Path & Coverage", showPathLayer) { showPathLayer = !showPathLayer }
                }
            }

            // Field Yield Summary
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CockpitSurface,
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("FIELD 18 SOUTH • YIELD & SOIL", color = DeereTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("AVG YIELD", color = DeereTextMuted, fontSize = 11.sp)
                        Text("228 BU/ACRE", color = StatusWorkingGreen, fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("SOIL MOISTURE", color = DeereTextMuted, fontSize = 11.sp)
                        Text("24.0% (OPTIMAL)", color = DeereYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("SOIL pH", color = DeereTextMuted, fontSize = 11.sp)
                        Text("6.5 pH", color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun LayerToggleRow(label: String, isChecked: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CockpitCard)
            .clickable { onToggle() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = DeereTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Switch(
            checked = isChecked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(checkedThumbColor = DeereYellow, checkedTrackColor = DeereGreen)
        )
    }
}
