package cl.duoc.maestranza_v2.ui.screens.users

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.model.User
import cl.duoc.maestranza_v2.ui.components.AddUserBottomSheet
import cl.duoc.maestranza_v2.ui.components.UserCard
import cl.duoc.maestranza_v2.ui.components.UsersFilterBottomSheet
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.MainViewModel
import cl.duoc.maestranza_v2.viewmodel.UsersViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreenMedium(
    navController: NavController,
    viewModel: MainViewModel,
    usersViewModel: UsersViewModel = viewModel()
) {
    val uiState by usersViewModel.uiState.collectAsState()
    var showFilters by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showToggleDialog by remember { mutableStateOf(false) }
    var showAddUser by remember { mutableStateOf(false) }
    var userToModify by remember { mutableStateOf<User?>(null) }

    cl.duoc.maestranza_v2.ui.components.ScaffoldWrapper(
        navController = navController,
        showDrawer = true,
        title = "Usuarios",
        actions = {
            IconButton(onClick = { showFilters = true }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtros")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddUser = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar usuario")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Campo de búsqueda
            OutlinedTextField(
                value = uiState.query,
                onValueChange = usersViewModel::onQueryChange,
                label = { Text("Buscar por usuario, nombre o email") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // Chips con filtros activos
            if (uiState.filters.selectedRoles.isNotEmpty() || uiState.filters.statusFilter != cl.duoc.maestranza_v2.model.UserStatusFilter.All) {
                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    uiState.filters.selectedRoles.forEach { role ->
                        val roleFormatted = when (role) {
                            "ROLE_ADMINISTRADOR" -> "Administrador"
                            "ROLE_AUDITOR" -> "Auditor"
                            "ROLE_COMPRAS" -> "Compras"
                            "ROLE_VENTAS" -> "Ventas"
                            "ROLE_SUPERVISOR" -> "Supervisor"
                            "ROLE_EMPLEADO" -> "Empleado"
                            else -> role
                        }
                        AssistChip(
                            onClick = { showFilters = true },
                            label = { Text(roleFormatted) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.FilterList,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                    }

                    if (uiState.filters.statusFilter != cl.duoc.maestranza_v2.model.UserStatusFilter.All) {
                        AssistChip(
                            onClick = { showFilters = true },
                            label = { Text(uiState.filters.statusFilter.name) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.FilterList,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // Lista de usuarios con cards
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.filteredUsers, key = { it.id }) { user ->
                    UserCard(
                        user = user,
                        onClick = { /* TODO: Detalle rápido */ },
                        onEdit = { userId ->
                            navController.navigate(
                                cl.duoc.maestranza_v2.navigation.Screen.EditUser.createRoute(userId)
                            )
                        },
                        onToggleActive = {
                            userToModify = user
                            showToggleDialog = true
                        },
                        onDelete = {
                            userToModify = user
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }

        // Bottom sheet de filtros
        if (showFilters) {
            UsersFilterBottomSheet(
                availableRoles = uiState.availableRoles,
                selectedRoles = uiState.filters.selectedRoles,
                statusFilter = uiState.filters.statusFilter,
                onRoleToggle = usersViewModel::onRoleFilterChange,
                onStatusFilterChange = usersViewModel::onStatusFilterChange,
                onClear = usersViewModel::clearFilters,
                onDismiss = { showFilters = false }
            )
        }

        // Diálogo de confirmación para activar/desactivar
        if (showToggleDialog && userToModify != null) {
            AlertDialog(
                onDismissRequest = {
                    showToggleDialog = false
                    userToModify = null
                },
                title = {
                    Text(text = if (userToModify!!.activo) "Desactivar usuario" else "Activar usuario")
                },
                text = {
                    Text(
                        text = if (userToModify!!.activo) {
                            "¿Desactivar al usuario \"${userToModify!!.username}\"? No podrá acceder al sistema."
                        } else {
                            "¿Activar al usuario \"${userToModify!!.username}\"?"
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            usersViewModel.toggleUserActive(userToModify!!.id)
                            showToggleDialog = false
                            userToModify = null
                        }
                    ) {
                        Text(if (userToModify!!.activo) "Desactivar" else "Activar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showToggleDialog = false
                        userToModify = null
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Diálogo de confirmación para eliminar
        if (showDeleteDialog && userToModify != null) {
            cl.duoc.maestranza_v2.ui.components.DeleteConfirmationDialog(
                showDialog = true,
                productName = userToModify!!.username,
                onDismiss = {
                    showDeleteDialog = false
                    userToModify = null
                },
                onConfirm = {
                    usersViewModel.deleteUser(userToModify!!.id)
                    showDeleteDialog = false
                    userToModify = null
                }
            )
        }

        // Bottom sheet para agregar usuario
        if (showAddUser) {
            AddUserBottomSheet(
                availableRoles = uiState.availableRoles,
                existingUsers = uiState.users,
                onDismiss = { showAddUser = false },
                onUserAdded = { newUser ->
                    usersViewModel.addUser(newUser)
                    showAddUser = false
                }
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "User Medium",
    device = "spec:width=800dp,height=1280dp,dpi=320"
)
@Composable
fun UserScreenMediumPreview() {
    Maestranza_V2Theme {
        UserScreenMedium(
            navController = rememberNavController(),
            viewModel = MainViewModel()
        )
    }
}