package com.example.model

enum class AlertSeverity {
    CRITICAL,
    WARNING,
    INFO
}

data class AlertItem(
    val id: String,
    val severity: AlertSeverity,
    val title: String,
    val description: String,
    val machineId: String,
    val machineName: String,
    val timestamp: String,
    val dtcCode: String?,
    val isAcknowledged: Boolean
)
