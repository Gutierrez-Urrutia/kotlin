package cl.duoc.maestranza_v2.ui.screens.editarUsuario

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.EditUserViewModel
import cl.duoc.maestranza_v2.viewmodel.MainViewModel
import cl.duoc.maestranza_v2.viewmodel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    usersViewModel: UsersViewModel,
    userId: String,
    editUserViewModel: EditUserViewModel = viewModel()
) {
    val state by editUserViewModel.state.collectAsState()
    val usersUiState by usersViewModel.uiState.collectAsState()
    val user = usersViewModel.getUserById(userId)

    // Cargar usuario cuando se monta la pantalla
    LaunchedEffect(userId) {
        user?.let {
            editUserViewModel.loadUser(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Usuario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (state.originalUser == null) {
            // Estado de carga o usuario no encontrado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoadingUser) {
                    CircularProgressIndicator()
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Usuario no encontrado",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Volver")
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header informativo
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Editando usuario:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = state.username,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        // Chip de estado
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(if (state.activo) "Activo" else "Inactivo")
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (state.activo) {
                                    MaterialTheme.colorScheme.tertiaryContainer
                                } else {
                                    MaterialTheme.colorScheme.errorContainer
                                },
                                labelColor = if (state.activo) {
                                    MaterialTheme.colorScheme.onTertiaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onErrorContainer
                                }
                            )
                        )
                    }
                }

                // Nombre
                OutlinedTextField(
                    value = state.nombre,
                    onValueChange = editUserViewModel::onNombreChange,
                    label = { Text("Nombre") },
                    placeholder = { Text("Ej: Juan") },
                    isError = state.errors.nombre != null,
                    supportingText = {
                        state.errors.nombre?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !state.isLoading
                )

                // Apellido
                OutlinedTextField(
                    value = state.apellido,
                    onValueChange = editUserViewModel::onApellidoChange,
                    label = { Text("Apellido") },
                    placeholder = { Text("Ej: Pérez") },
                    isError = state.errors.apellido != null,
                    supportingText = {
                        state.errors.apellido?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !state.isLoading
                )

                // Correo Electrónico
                OutlinedTextField(
                    value = state.email,
                    onValueChange = editUserViewModel::onEmailChange,
                    label = { Text("Correo Electrónico") },
                    placeholder = { Text("Ej: juan.perez@maestranza.cl") },
                    isError = state.errors.email != null,
                    supportingText = {
                        state.errors.email?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !state.isLoading
                )

                // Nombre de Usuario (readonly)
                OutlinedTextField(
                    value = state.username,
                    onValueChange = {},
                    label = { Text("Nombre de Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = false,
                    supportingText = {
                        Text(
                            text = "No editable",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                // Roles
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Roles *",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    usersUiState.availableRoles.forEach { role ->
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
                                    selected = state.selectedRoles.contains(role),
                                    onClick = { editUserViewModel.onRoleToggle(role) },
                                    role = Role.Checkbox,
                                    enabled = !state.isLoading
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = state.selectedRoles.contains(role),
                                onCheckedChange = null,
                                enabled = !state.isLoading
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = roleFormatted,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    state.errors.roles?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón Cancelar
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f),
                        enabled = !state.isLoading
                    ) {
                        Text("Cancelar")
                    }

                    // Botón Actualizar
                    Button(
                        onClick = {
                            if (editUserViewModel.validateForm(usersUiState.users)) {
                                editUserViewModel.setLoading(true)

                                // Actualizar usuario
                                val updatedUser = state.originalUser!!.copy(
                                    nombre = state.nombre,
                                    apellido = state.apellido,
                                    email = state.email,
                                    roles = state.selectedRoles.toList()
                                )

                                usersViewModel.updateUser(updatedUser)
                                editUserViewModel.setLoading(false)
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !state.isLoading && state.hasChanges
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Actualizar")
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun EditUserScreenPreview() {
    Maestranza_V2Theme {
        EditUserScreen(
            navController = rememberNavController(),
            mainViewModel = MainViewModel(),
            usersViewModel = UsersViewModel(),
            userId = "1"
        )
    }
}

