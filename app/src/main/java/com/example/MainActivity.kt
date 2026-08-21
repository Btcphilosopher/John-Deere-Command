package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.NavigationTabBar
import com.example.ui.components.TopCockpitHeader
import com.example.ui.screens.*
import com.example.ui.theme.CockpitBackground
import com.example.ui.theme.JohnDeereTheme
import com.example.viewmodel.JohnDeereViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JohnDeereTheme {
                val viewModel: JohnDeereViewModel = viewModel()

                val machines by viewModel.machines.collectAsState()
                val fields by viewModel.fields.collectAsState()
                val jobs by viewModel.jobs.collectAsState()
                val alerts by viewModel.alerts.collectAsState()
                val maintenance by viewModel.maintenance.collectAsState()
                val hotspots by viewModel.hotspots.collectAsState()
                val auditLogs by viewModel.auditLogs.collectAsState()
                val selectedMachineId by viewModel.selectedMachineId.collectAsState()
                val selectedMachine by viewModel.selectedMachine.collectAsState()
                val activeTab by viewModel.activeTab.collectAsState()
                val currentRole by viewModel.currentRole.collectAsState()
                val isMobileView by viewModel.isMobileView.collectAsState()
                val isSimulationRunning by viewModel.isSimulationRunning.collectAsState()
                val cockpitSpeed by viewModel.cockpitSpeed.collectAsState()
                val implementDepthCm by viewModel.implementDepthCm.collectAsState()
                val controlMode by viewModel.controlMode.collectAsState()
                val selectedCategoryFilter by viewModel.selectedCategoryFilter.collectAsState()
                val selectedStatusFilter by viewModel.selectedStatusFilter.collectAsState()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CockpitBackground),
                    topBar = {
                        TopCockpitHeader(
                            connectedMachineCount = machines.size,
                            unacknowledgedAlertsCount = alerts.count { !it.isAcknowledged },
                            currentRole = currentRole,
                            onRoleSelected = { viewModel.setUserRole(it) },
                            isMobileView = isMobileView,
                            onToggleMobileView = { viewModel.toggleMobileView() },
                            isSimulationRunning = isSimulationRunning,
                            onToggleSimulation = { viewModel.toggleSimulation() }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(CockpitBackground)
                    ) {
                        if (isMobileView) {
                            MobileCompanionScreen(
                                machines = machines,
                                alerts = alerts,
                                onSelectMachine = { viewModel.selectMachine(it) },
                                onNavigateToTablet = { viewModel.toggleMobileView() },
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            NavigationTabBar(
                                selectedTab = activeTab,
                                onTabSelected = { viewModel.setActiveTab(it) }
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            ) {
                                when (activeTab) {
                                    0 -> TabletCommandCenterScreen(
                                        machines = machines,
                                        fields = fields,
                                        selectedMachineId = selectedMachineId,
                                        onSelectMachine = { viewModel.selectMachine(it) },
                                        onNavigateToTab = { viewModel.setActiveTab(it) },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    1 -> LiveFarmMapScreen(
                                        fields = fields,
                                        machines = machines,
                                        selectedMachineId = selectedMachineId,
                                        onSelectMachine = { viewModel.selectMachine(it) },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    2 -> MachineControlCenterScreen(
                                        machine = selectedMachine,
                                        hotspots = hotspots,
                                        cockpitSpeed = cockpitSpeed,
                                        onSpeedChange = { viewModel.setCockpitSpeed(it) },
                                        implementDepthCm = implementDepthCm,
                                        onDepthChange = { viewModel.setImplementDepth(it) },
                                        controlMode = controlMode,
                                        onModeChange = { viewModel.setControlMode(it) },
                                        onStartOperation = { viewModel.startMachineOperation() },
                                        onPauseJob = { viewModel.pauseJobOperation() },
                                        onResumeJob = { viewModel.resumeJobOperation() },
                                        onStopOperation = { viewModel.stopOperation() },
                                        onEmergencySafeState = { viewModel.emergencySafeStateReturn() },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    3 -> FleetManagementScreen(
                                        machines = machines,
                                        selectedCategory = selectedCategoryFilter,
                                        onCategoryFilterChange = { viewModel.setCategoryFilter(it) },
                                        selectedStatus = selectedStatusFilter,
                                        onStatusFilterChange = { viewModel.setStatusFilter(it) },
                                        onSelectMachine = { viewModel.selectMachine(it) },
                                        onNavigateToCockpit = { viewModel.setActiveTab(2) },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    4 -> PrecisionAgScreen(
                                        fields = fields,
                                        machines = machines,
                                        selectedMachineId = selectedMachineId,
                                        onSelectMachine = { viewModel.selectMachine(it) },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    5 -> JobManagementScreen(
                                        jobs = jobs,
                                        fields = fields,
                                        machines = machines,
                                        onDispatchJob = { title, type, fieldId, machineId, implement, operator, rate ->
                                            viewModel.dispatchNewJob(title, type, fieldId, machineId, implement, operator, rate)
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    6 -> AutonomousSupervisionScreen(
                                        machines = machines,
                                        onSelectMachine = { viewModel.selectMachine(it) },
                                        onPauseMachine = { viewModel.pauseJobOperation() },
                                        onEmergencySafeState = { viewModel.emergencySafeStateReturn() },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    7 -> AlertCenterScreen(
                                        alerts = alerts,
                                        onAcknowledgeAlert = { viewModel.acknowledgeAlert(it) },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    8 -> MaintenanceDiagnosticsScreen(
                                        maintenanceItems = maintenance,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    9 -> ConnectivityNetworkScreen(
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    10 -> AnalyticsEconomicsScreen(
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    11 -> SecurityAuditScreen(
                                        auditLogs = auditLogs,
                                        currentRole = currentRole,
                                        onRoleSelected = { viewModel.setUserRole(it) },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
