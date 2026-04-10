package com.stockapp.ui

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stockapp.ui.navigation.StockNavGraph
import com.stockapp.ui.navigation.bottomNavItems

@Composable
fun StockAppContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavRoutes = bottomNavItems.map { it.screen.route }
    val showBottomBar = currentDestination?.route in bottomNavRoutes

    val currentTabIndex = bottomNavItems.indexOfFirst { it.screen.route == currentDestination?.route }

    fun navigateToTab(index: Int) {
        navController.navigate(bottomNavItems[index].screen.route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    var swipeOffset by remember { mutableFloatStateOf(0f) }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.screen.route
                        } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navigateToTab(bottomNavItems.indexOf(item)) },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        val swipeModifier = if (showBottomBar && currentTabIndex >= 0) {
            Modifier.pointerInput(currentTabIndex) {
                detectHorizontalDragGestures(
                    onDragStart = { swipeOffset = 0f },
                    onDragEnd = {
                        val threshold = 80.dp.toPx()
                        when {
                            swipeOffset < -threshold && currentTabIndex < bottomNavItems.size - 1 ->
                                navigateToTab(currentTabIndex + 1)
                            swipeOffset > threshold && currentTabIndex > 0 ->
                                navigateToTab(currentTabIndex - 1)
                        }
                        swipeOffset = 0f
                    },
                    onHorizontalDrag = { _, dragAmount -> swipeOffset += dragAmount }
                )
            }
        } else Modifier

        StockNavGraph(
            navController = navController,
            modifier = swipeModifier.padding(innerPadding)
        )
    }
}
