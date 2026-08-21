package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.Field
import com.example.model.Machine
import com.example.ui.components.LiveFarmMapCanvas

@Composable
fun LiveFarmMapScreen(
    fields: List<Field>,
    machines: List<Machine>,
    selectedMachineId: String?,
    onSelectMachine: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        LiveFarmMapCanvas(
            fields = fields,
            machines = machines,
            selectedMachineId = selectedMachineId,
            onMachineSelected = onSelectMachine,
            modifier = Modifier.fillMaxSize(),
            showYieldLayer = true,
            showGuidanceLayer = true,
            showTrailsLayer = true
        )
    }
}
