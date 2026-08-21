package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun TopCockpitHeader(
    connectedMachineCount: Int,
    unacknowledgedAlertsCount: Int,
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    isMobileView: Boolean,
    onToggleMobileView: () -> Unit,
    isSimulationRunning: Boolean,
    onToggleSimulation: () -> Unit
) {
    var showRoleDropdown by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CockpitBorder, RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)),
        color = CockpitSurface,
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Branding Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // John Deere Logo Icon / Badge
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DeereGreen)
                        .border(1.5.dp, DeereYellow, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        color = DeereYellow,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "JOHN DEERE",
                            color = DeereTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(DeereYellow.copy(alpha = 0.2f))
                                .border(0.5.dp, DeereYellow, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "COMMAND",
                                color = DeereYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(
                            imageVector = Icons.Default.Agriculture,
                            contentDescription = "Farm",
                            tint = DeereTextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "GREEN PRAIRIE FARMS • OAKLAND SECTOR",
                            color = DeereTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Middle Status Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Connected Fleet Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(CockpitCard)
                        .border(1.dp, StatusWorkingGreen.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(StatusWorkingGreen)
                    )
                    Text(
                        text = "$connectedMachineCount CONNECTED",
                        color = StatusWorkingGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Simulation Control Toggle
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSimulationRunning) DeereGreenDark else CockpitCard)
                        .clickable { onToggleSimulation() }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (isSimulationRunning) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = "Sim Toggle",
                        tint = if (isSimulationRunning) StatusWorkingGreen else DeereTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (isSimulationRunning) "SIM: LIVE" else "SIM: PAUSED",
                        color = if (isSimulationRunning) DeereTextPrimary else DeereTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Alerts Pill
                if (unacknowledgedAlertsCount > 0) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(StatusErrorRed.copy(alpha = 0.2f))
                            .border(1.dp, StatusErrorRed, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alerts",
                            tint = StatusErrorRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "$unacknowledgedAlertsCount ALERTS",
                            color = StatusErrorRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Right Info & Role Selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Weather Widget
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Weather",
                        tint = DeereYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "72°F • 8 mph SW",
                        color = DeereTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Tablet vs Mobile View Mode Switcher
                IconButton(
                    onClick = onToggleMobileView,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isMobileView) DeereYellow.copy(alpha = 0.2f) else CockpitCard)
                        .border(1.dp, if (isMobileView) DeereYellow else CockpitBorder, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = if (isMobileView) Icons.Default.Smartphone else Icons.Default.Tablet,
                        contentDescription = "Toggle Device View",
                        tint = if (isMobileView) DeereYellow else DeereTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Role Selector Button
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CockpitCard)
                            .border(1.dp, CockpitBorderActive, RoundedCornerShape(8.dp))
                            .clickable { showRoleDropdown = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Role",
                            tint = DeereYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Column {
                            Text(
                                text = currentRole.title,
                                color = DeereTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = DeereTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showRoleDropdown,
                        onDismissRequest = { showRoleDropdown = false },
                        modifier = Modifier.background(CockpitSurface)
                    ) {
                        UserRole.entries.forEach { role ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(role.title, color = DeereTextPrimary, fontWeight = FontWeight.Bold)
                                        Text(role.description, color = DeereTextSecondary, fontSize = 10.sp)
                                    }
                                },
                                onClick = {
                                    onRoleSelected(role)
                                    showRoleDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
