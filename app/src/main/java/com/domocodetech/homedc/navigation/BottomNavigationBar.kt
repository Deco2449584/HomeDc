package com.domocodetech.homedc.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.domocodetech.homedc.R

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Routes.HOME_ROUTE, R.drawable.baseline_home_24),
        BottomNavItem("Explorer", Routes.EXPLORER_ROUTE, R.drawable.baseline_search_24),
        BottomNavItem("Profile", Routes.PROFILE_ROUTE, R.drawable.baseline_supervised_user_circle_24)
    )

    val colorScheme = MaterialTheme.colorScheme

    BottomNavigation(
        backgroundColor = colorScheme.background,
        contentColor = colorScheme.onBackground,
        modifier = Modifier.padding(8.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                selectedContentColor = colorScheme.primary,
                unselectedContentColor = colorScheme.onBackground.copy(alpha = 0.6f),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: Int)