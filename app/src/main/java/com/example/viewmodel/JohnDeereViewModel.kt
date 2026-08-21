package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import com.example.simulation.FarmSimulationEngine
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JohnDeereViewModel : ViewModel() {

    private val _machines = MutableStateFlow<List<Machine>>(FarmSimulationEngine.initialMachines())
    val machines: StateFlow<List<Machine>> = _machines.asStateFlow()

    private val _fields = MutableStateFlow<List<Field>>(FarmSimulationEngine.initialFields())
    val fields: StateFlow<List<Field>> = _fields.asStateFlow()

    private val _jobs = MutableStateFlow<List<Job>>(FarmSimulationEngine.initialJobs())
    val jobs: StateFlow<List<Job>> = _jobs.asStateFlow()

    private val _alerts = MutableStateFlow<List<AlertItem>>(FarmSimulationEngine.initialAlerts())
    val alerts: StateFlow<List<AlertItem>> = _alerts.asStateFlow()

    private val _maintenance = MutableStateFlow<List<MaintenanceItem>>(FarmSimulationEngine.initialMaintenance())
    val maintenance: StateFlow<List<MaintenanceItem>> = _maintenance.asStateFlow()

    private val _hotspots = MutableStateFlow<List<HotspotComponent>>(FarmSimulationEngine.initialHotspots())
    val hotspots: StateFlow<List<HotspotComponent>> = _hotspots.asStateFlow()

    private val _auditLogs = MutableStateFlow<List<CommandAuditRecord>>(
        listOf(
            CommandAuditRecord("cmd-1", "James Miller", "Cab Operator", "JOHN DEERE 8R 410", "Start Job Pass 18", "14:32:05", "ACCEPTED", "ACKNOWLEDGED — Engine RPM 1850"),
            CommandAuditRecord("cmd-2", "Sarah Jenkins", "Fleet Manager", "JOHN DEERE X9 1100", "Calibrate Grain Sensor", "14:15:10", "ACCEPTED", "ACKNOWLEDGED — Yield Sensor Ready"),
            CommandAuditRecord("cmd-3", "Autonomous Engine", "System Auto", "8R 370 AUTONOMOUS", "Obstacle Scan Clearance", "13:58:44", "ACCEPTED", "ACKNOWLEDGED — Path Clear 0.0m Shift")
        )
    )
    val auditLogs: StateFlow<List<CommandAuditRecord>> = _auditLogs.asStateFlow()

    private val _selectedMachineId = MutableStateFlow<String?>("jd-8r410-01")
    val selectedMachineId: StateFlow<String?> = _selectedMachineId.asStateFlow()

    val selectedMachine: StateFlow<Machine?> = combine(_machines, _selectedMachineId) { list, id ->
        list.find { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _activeTab = MutableStateFlow(0) // 0: Command Center, 1: Map, 2: Cockpit, 3: Fleet, etc.
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.FLEET_MANAGER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _isMobileView = MutableStateFlow(false)
    val isMobileView: StateFlow<Boolean> = _isMobileView.asStateFlow()

    private val _isSimulationRunning = MutableStateFlow(true)
    val isSimulationRunning: StateFlow<Boolean> = _isSimulationRunning.asStateFlow()

    // Command parameters for selected machine cockpit controls
    private val _cockpitSpeed = MutableStateFlow(8.4f)
    val cockpitSpeed: StateFlow<Float> = _cockpitSpeed.asStateFlow()

    private val _implementDepthCm = MutableStateFlow(12)
    val implementDepthCm: StateFlow<Int> = _implementDepthCm.asStateFlow()

    private val _controlMode = MutableStateFlow("AUTO") // AUTO, ASSIST, MANUAL
    val controlMode: StateFlow<String> = _controlMode.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<MachineCategory?>(null)
    val selectedCategoryFilter: StateFlow<MachineCategory?> = _selectedCategoryFilter.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow<MachineStatus?>(null)
    val selectedStatusFilter: StateFlow<MachineStatus?> = _selectedStatusFilter.asStateFlow()

    init {
        startSimulationLoop()
    }

    private fun startSimulationLoop() {
        viewModelScope.launch {
            FarmSimulationEngine.simulationStream(_machines.value).collect { updated ->
                if (_isSimulationRunning.value) {
                    _machines.value = updated
                }
            }
        }
    }

    fun selectMachine(id: String) {
        _selectedMachineId.value = id
        _machines.value.find { it.id == id }?.let { m ->
            _cockpitSpeed.value = m.speedKmH
        }
    }

    fun setActiveTab(tabIndex: Int) {
        _activeTab.value = tabIndex
    }

    fun setUserRole(role: UserRole) {
        _currentRole.value = role
    }

    fun toggleMobileView() {
        _isMobileView.value = !_isMobileView.value
    }

    fun toggleSimulation() {
        _isSimulationRunning.value = !_isSimulationRunning.value
    }

    fun setCategoryFilter(category: MachineCategory?) {
        _selectedCategoryFilter.value = category
    }

    fun setStatusFilter(status: MachineStatus?) {
        _selectedStatusFilter.value = status
    }

    fun setCockpitSpeed(speed: Float) {
        _cockpitSpeed.value = speed
    }

    fun setImplementDepth(depth: Int) {
        _implementDepthCm.value = depth
    }

    fun setControlMode(mode: String) {
        _controlMode.value = mode
        recordAudit("Change Mode to $mode")
    }

    // Machine Command Actions with Audit Logging
    fun startMachineOperation() {
        val current = selectedMachine.value ?: return
        _machines.value = _machines.value.map {
            if (it.id == current.id) it.copy(status = MachineStatus.WORKING, speedKmH = 8.0f, engineRpm = 1850) else it
        }
        recordAudit("Start Machine Operation (Mode: ${_controlMode.value})")
    }

    fun pauseJobOperation() {
        val current = selectedMachine.value ?: return
        _machines.value = _machines.value.map {
            if (it.id == current.id) it.copy(status = MachineStatus.STANDBY, speedKmH = 0.0f, engineRpm = 900) else it
        }
        recordAudit("Pause Job Operation")
    }

    fun resumeJobOperation() {
        val current = selectedMachine.value ?: return
        _machines.value = _machines.value.map {
            if (it.id == current.id) it.copy(status = MachineStatus.WORKING, speedKmH = _cockpitSpeed.value, engineRpm = 1800) else it
        }
        recordAudit("Resume Job Operation")
    }

    fun stopOperation() {
        val current = selectedMachine.value ?: return
        _machines.value = _machines.value.map {
            if (it.id == current.id) it.copy(status = MachineStatus.STANDBY, speedKmH = 0.0f, engineRpm = 0) else it
        }
        recordAudit("Stop Operation (Engine Shutdown)")
    }

    fun emergencySafeStateReturn() {
        val current = selectedMachine.value ?: return
        _machines.value = _machines.value.map {
            if (it.id == current.id) it.copy(
                status = MachineStatus.STANDBY,
                speedKmH = 0.0f,
                engineRpm = 800,
                isAutonomous = false
            ) else it
        }
        recordAudit("EMERGENCY RETURN TO SAFE STATE TRIGGERED", status = "ACCEPTED - EMERGENCY")
    }

    fun acknowledgeAlert(alertId: String) {
        _alerts.value = _alerts.value.map {
            if (it.id == alertId) it.copy(isAcknowledged = true) else it
        }
        recordAudit("Acknowledged Alert $alertId")
    }

    fun dispatchNewJob(
        title: String,
        type: JobType,
        fieldId: String,
        machineId: String,
        implement: String,
        operator: String,
        targetRate: String
    ) {
        val field = _fields.value.find { it.id == fieldId }
        val machine = _machines.value.find { it.id == machineId }
        val newJob = Job(
            id = "job-${System.currentTimeMillis().toString().takeLast(4)}",
            title = title.ifBlank { "NEW DISPATCH JOB" },
            type = type,
            fieldId = fieldId,
            fieldName = field?.name ?: "UNASSIGNED FIELD",
            machineId = machineId,
            machineName = machine?.name ?: "UNASSIGNED MACHINE",
            implementName = implement,
            operatorName = operator,
            targetRate = targetRate,
            progressPercent = 0f,
            status = JobStatus.ACTIVE,
            startTime = SimpleDateFormat("hh:mm a", Locale.US).format(Date()),
            estEndTime = "06:00 PM",
            acresTarget = field?.totalAcres ?: 100f,
            acresCompleted = 0f
        )
        _jobs.value = listOf(newJob) + _jobs.value
        recordAudit("Dispatched Job: ${newJob.title} to ${newJob.machineName}")
    }

    private fun recordAudit(actionCommand: String, status: String = "ACCEPTED") {
        val timeStr = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        val machineName = selectedMachine.value?.name ?: "Fleet System"
        val record = CommandAuditRecord(
            id = "cmd-${System.currentTimeMillis()}",
            operatorName = if (_currentRole.value == UserRole.OPERATOR) "James Miller" else "Farm Admin (${_currentRole.value.name})",
            userRole = _currentRole.value.title,
            machineName = machineName,
            actionCommand = actionCommand,
            timestamp = timeStr,
            status = status,
            machineResponse = "ACKNOWLEDGED BY MACHINE BUS"
        )
        _auditLogs.value = listOf(record) + _auditLogs.value
    }
}
