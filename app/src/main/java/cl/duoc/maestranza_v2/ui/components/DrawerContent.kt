package cl.duoc.maestranza_v2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.navigation.Screen

data class DrawerMenuItem(
    val icon: ImageVector,
    val title: String,
    val screen: Screen?
)

@Composable
fun DrawerContent(
    navController: NavController,
    currentRoute: String?,
    onItemClick: () -> Unit
) {
    val menuItems = listOf(
        DrawerMenuItem(Icons.Default.Inventory, "Inventario", Screen.Inventory),
        DrawerMenuItem(Icons.Default.SwapHoriz, "Movimientos", Screen.Movements),
        DrawerMenuItem(Icons.Default.Person, "Usuarios", Screen.Users)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 16.dp)
    ) {
        // Items del menú
        menuItems.forEach { item ->
            DrawerItem(
                icon = item.icon,
                title = item.title,
                selected = currentRoute == item.screen?.route,
                onClick = {
                    item.screen?.let { screen ->
                        navController.navigate(screen.route) {
                            // Evitar múltiples copias de la misma pantalla en el back stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        onItemClick()
                    }
                    // Si no hay pantalla (como Movimientos), no hacer nada
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Item de cerrar sesión
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        DrawerItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Cerrar Sesión",
            selected = false,
            onClick = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
                onItemClick()
            }
        )
    }
}


@Composable
private fun DrawerItem(
    icon: ImageVector,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor,
            fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.Bold else null
        )
    }
}

