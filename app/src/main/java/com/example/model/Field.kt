package com.example.model

data class LatLngPoint(val lat: Double, val lng: Double)

data class Field(
    val id: String,
    val name: String,
    val totalAcres: Float,
    val cropType: String,
    val boundaryPoints: List<LatLngPoint>,
    val guidanceLinesCount: Int,
    val soilPh: Float,
    val soilMoisturePercent: Float,
    val avgYieldBushelsPerAcre: Float,
    val completionPercent: Float,
    val hazardsCount: Int,
    val assignedMachineId: String?
)
