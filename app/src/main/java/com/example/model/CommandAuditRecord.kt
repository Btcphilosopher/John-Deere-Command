package com.example.model

data class CommandAuditRecord(
    val id: String,
    val operatorName: String,
    val userRole: String,
    val machineName: String,
    val actionCommand: String,
    val timestamp: String,
    val status: String, // ACCEPTED, PENDING, REJECTED, ACKNOWLEDGED
    val machineResponse: String,
    val isSimulated: Boolean = true
)
