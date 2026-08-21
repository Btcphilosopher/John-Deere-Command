package com.example.model

enum class UserRole(val title: String, val description: String) {
    OPERATOR("Cab Operator", "Simplified cockpit view with primary machine controls & alerts"),
    SUPERVISOR("Field Supervisor", "Mid-level control for field jobs and machine assignment"),
    FLEET_MANAGER("Fleet Manager", "Full fleet monitoring, analytics, maintenance & dispatch"),
    FARM_MANAGER("Farm Manager", "Enterprise overview, economics, fields & high-level reporting"),
    TECHNICIAN("Service Technician", "Deep diagnostic logs, subsystem telemetry & maintenance mode"),
    ADMINISTRATOR("System Administrator", "Full system permissions, role management & security audit")
}
