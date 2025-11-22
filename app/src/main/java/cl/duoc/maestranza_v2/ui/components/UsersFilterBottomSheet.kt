package cl.duoc.maestranza_v2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import cl.duoc.maestranza_v2.model.User
import cl.duoc.maestranza_v2.model.UserStatusFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersFilterBottomSheet(
    availableRoles: List<String>,
    selectedRoles: Set<String>,
    statusFilter: UserStatusFilter,
    onRoleToggle: (String) -> Unit,
    onStatusFilterChange: (UserStatusFilter) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtros de Usuarios",
                    style = MaterialTheme.typography.headlineSmall
                )
                TextButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Limpiar")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Filtro de estado con segmented button
            Text(
                text = "Estado",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    selected = statusFilter == UserStatusFilter.All,
                    onClick = { onStatusFilterChange(UserStatusFilter.All) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                ) {
                    Text("Todos")
                }
                SegmentedButton(
                    selected = statusFilter == UserStatusFilter.Active,
                    onClick = { onStatusFilterChange(UserStatusFilter.Active) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                ) {
                    Text("Activos")
                }
                SegmentedButton(
                    selected = statusFilter == UserStatusFilter.Inactive,
                    onClick = { onStatusFilterChange(UserStatusFilter.Inactive) },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                ) {
                    Text("Inactivos")
                }
            }

            Spacer(Modifier.height(24.dp))

            // Roles con checkboxes
            Text(
                text = "Roles",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))

            availableRoles.forEach { role ->
                val roleFormatted = when (role) {
                    "ROLE_ADMINISTRADOR" -> "Administrador"
                    "ROLE_AUDITOR" -> "Auditor"
                    "ROLE_COMPRAS" -> "Compras"
                    "ROLE_VENTAS" -> "Ventas"
                    "ROLE_SUPERVISOR" -> "Supervisor"
                    "ROLE_EMPLEADO" -> "Empleado"
                    else -> role.removePrefix("ROLE_")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedRoles.contains(role),
                            onClick = { onRoleToggle(role) },
                            role = Role.Checkbox
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedRoles.contains(role),
                        onCheckedChange = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = roleFormatted,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Botón aplicar
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aplicar Filtros")
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

