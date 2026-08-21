package com.example.model

enum class JobType {
    TILLAGE,
    PLANTING,
    SPRAYING,
    HARVESTING,
    TRANSPORT,
    INSPECTION
}

enum class JobStatus {
    ACTIVE,
    PENDING,
    PAUSED,
    COMPLETED
}

data class Job(
    val id: String,
    val title: String,
    val type: JobType,
    val fieldId: String,
    val fieldName: String,
    val machineId: String,
    val machineName: String,
    val implementName: String,
    val operatorName: String,
    val targetRate: String,
    val progressPercent: Float,
    val status: JobStatus,
    val startTime: String,
    val estEndTime: String,
    val acresTarget: Float,
    val acresCompleted: Float
)
