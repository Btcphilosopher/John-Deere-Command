package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class TabItem(val index: Int, val title: String, val icon: ImageVector)

val PLATFORM_TABS = listOf(
    TabItem(0, "COMMAND CENTER", Icons.Default.Dashboard),
    TabItem(1, "LIVE FARM MAP", Icons.Default.Map),
    TabItem(2, "MACHINE COCKPIT", Icons.Default.Agriculture),
    TabItem(3, "FLEET MANAGEMENT", Icons.Default.LocalShipping),
    TabItem(4, "PRECISION AG", Icons.Default.Layers),
    TabItem(5, "JOB DISPATCH", Icons.Default.Assignment),
    TabItem(6, "AUTONOMOUS SUPERVISION", Icons.Default.PrecisionManufacturing),
    TabItem(7, "ALERTS & DIAGNOSTICS", Icons.Default.Warning),
    TabItem(8, "MAINTENANCE", Icons.Default.Build),
    TabItem(9, "CONNECTIVITY & BUS", Icons.Default.Wifi),
    TabItem(10, "ANALYTICS & ECONOMICS", Icons.Default.BarChart),
    TabItem(11, "SECURITY & AUDIT", Icons.Default.Security)
)

@Composable
fun NavigationTabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CockpitBackground)
            .border(1.dp, CockpitBorder)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        PLATFORM_TABS.forEach { tab ->
            val isSelected = selectedTab == tab.index

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) DeereGreen else CockpitSurface)
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) DeereYellow else CockpitBorder,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onTabSelected(tab.index) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.title,
                    tint = if (isSelected) DeereYellow else DeereTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = tab.title,
                    color = if (isSelected) DeereTextPrimary else DeereTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
