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
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun JobManagementScreen(
    jobs: List<Job>,
    fields: List<Field>,
    machines: List<Machine>,
    onDispatchJob: (title: String, type: JobType, fieldId: String, machineId: String, implement: String, operator: String, targetRate: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDispatchDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top Header with Dispatch Job Button
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
                    Text("JOB DISPATCH & FIELD OPERATIONS", color = DeereYellow, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("ACTIVE JOBS (${jobs.count { it.status == JobStatus.ACTIVE }} RUNNING)", color = DeereTextSecondary, fontSize = 11.sp)
                }

                Button(
                    onClick = { showDispatchDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = DeereGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Job", tint = DeereYellow, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("DISPATCH NEW JOB", color = DeereYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Job Cards List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(jobs) { job ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CockpitSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CockpitBorder))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(job.title, color = DeereTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(StatusWorkingGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(job.status.name, color = StatusWorkingGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Text("TARGET: ${job.targetRate}", color = DeereYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Text("FIELD: ${job.fieldName} • MACHINE: ${job.machineName} (${job.implementName}) • OP: ${job.operatorName}", color = DeereTextSecondary, fontSize = 11.sp)

                        // Progress Bar
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("ACRES: ${job.acresCompleted.toInt()} / ${job.acresTarget.toInt()} AC", color = DeereTextMuted, fontSize = 10.sp)
                                Text("${job.progressPercent.toInt()}% COMPLETE", color = StatusWorkingGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            LinearProgressIndicator(
                                progress = { job.progressPercent / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = StatusWorkingGreen,
                                trackColor = CockpitCard
                            )
                        }
                    }
                }
            }
        }
    }

    // New Job Dispatch Dialog Modal
    if (showDispatchDialog) {
        var jobTitle by remember { mutableStateOf("AUTONOMOUS TILLAGE PASS") }
        var targetRate by remember { mutableStateOf("15.0 Acres/Hr") }
        var selectedFieldId by remember { mutableStateOf(fields.firstOrNull()?.id ?: "") }
        var selectedMachineId by remember { mutableStateOf(machines.firstOrNull()?.id ?: "") }
        var implementText by remember { mutableStateOf("2680H High Speed Disk") }
        var operatorText by remember { mutableStateOf("James Miller") }

        AlertDialog(
            onDismissRequest = { showDispatchDialog = false },
            title = { Text("DISPATCH NEW FIELD JOB", color = DeereYellow, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = jobTitle,
                        onValueChange = { jobTitle = it },
                        label = { Text("Job Title") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = targetRate,
                        onValueChange = { targetRate = it },
                        label = { Text("Target Application Rate") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = implementText,
                        onValueChange = { implementText = it },
                        label = { Text("Implement Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = operatorText,
                        onValueChange = { operatorText = it },
                        label = { Text("Assigned Operator") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDispatchJob(jobTitle, JobType.TILLAGE, selectedFieldId, selectedMachineId, implementText, operatorText, targetRate)
                        showDispatchDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeereGreen)
                ) {
                    Text("DISPATCH JOB", color = DeereYellow, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDispatchDialog = false }) {
                    Text("CANCEL", color = DeereTextSecondary)
                }
            },
            containerColor = CockpitSurface
        )
    }
}
