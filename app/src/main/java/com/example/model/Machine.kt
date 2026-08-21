package com.example.model

enum class MachineCategory {
    TRACTOR,
    COMBINE_HARVESTER,
    SPRAYER,
    PLANTER_SEEDER,
    MOWER_UTILITY,
    AUTONOMOUS_UNIT
}

enum class MachineStatus {
    WORKING,
    HARVESTING,
    STANDBY,
    MAINTENANCE,
    TRANSIT,
    ERROR
}

enum class ConnectivityType {
    CELLULAR_5G,
    GNSS_RTK,
    ISOBUS_CLASS_3,
    JDLINK_CLOUD
}

data class Machine(
    val id: String,
    val name: String,
    val modelNumber: String,
    val category: MachineCategory,
    val status: MachineStatus,
    val fuelPercent: Float, // 0 to 100
    val speedKmH: Float,
    val engineRpm: Int,
    val engineLoadPercent: Float,
    val hydraulicTempC: Float,
    val latitude: Double,
    val longitude: Double,
    val headingDegrees: Float,
    val currentFieldId: String,
    val currentFieldName: String,
    val currentJobTitle: String,
    val operatorName: String,
    val implementAttached: String,
    val productivityAcresPerHr: Float,
    val acresCompletedToday: Float,
    val totalMachineHours: Float,
    val isAutonomous: Boolean,
    val connectivity: ConnectivityType,
    val connectivitySignalPercent: Int,
    val alertCount: Int,
    val healthPercent: Float
)
