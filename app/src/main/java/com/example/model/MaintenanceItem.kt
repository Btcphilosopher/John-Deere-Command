package com.example.model

enum class ServiceStatus {
    OK,
    DUE_SOON,
    OVERDUE
}

data class MaintenanceItem(
    val id: String,
    val machineId: String,
    val machineName: String,
    val componentName: String,
    val remainingHours: Float,
    val intervalHours: Float,
    val status: ServiceStatus,
    val assignedTechnician: String,
    val lastServicedDate: String
)
