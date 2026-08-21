package com.example.model

data class HotspotComponent(
    val id: String,
    val name: String,
    val category: String, // Engine, Cab, Hydraulics, Implement, Tracks
    val healthPercent: Int,
    val tempC: Float?,
    val pressurePsi: Float?,
    val xRatio: Float, // 0.0 to 1.0 relative to chassis
    val yRatio: Float, // 0.0 to 1.0 relative to chassis
    val statusText: String,
    val isWarning: Boolean
)
