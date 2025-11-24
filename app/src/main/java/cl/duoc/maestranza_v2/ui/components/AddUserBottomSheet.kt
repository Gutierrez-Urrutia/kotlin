package cl.duoc.maestranza_v2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.maestranza_v2.data.model.User
import cl.duoc.maestranza_v2.viewmodel.AddUserViewModel
import cl.duoc.maestranza_v2.viewmodel.UsersViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserBottomSheet(
    availableRoles: List<String>,
    existingUsers: List<User>,
    onDismiss: () -> Unit,
    onUserAdded: (User) -> Unit,
    usersViewModel: UsersViewModel? = null,
    addUserViewModel: AddUserViewModel = viewModel()
) {
    val state by addUserViewModel.state.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Diálogo de confirmación para descartar cambios
    if (state.hasChanges) {
        BackHandler(enabled = true) {
            // Mostrar confirmación antes de cerrar
            onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            // Header fijo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agregar Usuario",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar"
                    )
                }
            }

            HorizontalDivider()

            // Body scrolleable
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mostrar mensaje de error si hay
                errorMessage?.let { error ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = error,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Nombre
                OutlinedTextField(
                    value = state.nombre,
                    onValueChange = addUserViewModel::onNombreChange,
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
                    onValueChange = addUserViewModel::onApellidoChange,
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

                // Nombre de Usuario
                OutlinedTextField(
                    value = state.username,
                    onValueChange = addUserViewModel::onUsernameChange,
                    label = { Text("Nombre de Usuario") },
                    placeholder = { Text("Ej: jperez") },
                    isError = state.errors.username != null,
                    supportingText = {
                        state.errors.username?.let {
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
                    onValueChange = addUserViewModel::onEmailChange,
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

                // Contraseña
                OutlinedTextField(
                    value = state.password,
                    onValueChange = addUserViewModel::onPasswordChange,
                    label = { Text("Contraseña") },
                    placeholder = { Text("Mínimo 6 caracteres") },
                    isError = state.errors.password != null,
                    supportingText = {
                        state.errors.password?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    visualTransformation = if (state.showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = addUserViewModel::togglePasswordVisibility) {
                            Icon(
                                imageVector = if (state.showPassword) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (state.showPassword) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !state.isLoading
                )

                // Confirmar Contraseña
                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = addUserViewModel::onConfirmPasswordChange,
                    label = { Text("Confirmar Contraseña") },
                    placeholder = { Text("Repite la contraseña") },
                    isError = state.errors.confirmPassword != null,
                    supportingText = {
                        state.errors.confirmPassword?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    visualTransformation = if (state.showConfirmPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = addUserViewModel::toggleConfirmPasswordVisibility) {
                            Icon(
                                imageVector = if (state.showConfirmPassword) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (state.showConfirmPassword) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !state.isLoading
                )

                // Roles
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Roles *",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    availableRoles.forEach { role ->
                        val roleFormatted = formatRolDisplayName(role)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = state.selectedRoles.contains(role),
                                    onClick = { addUserViewModel.onRoleToggle(role) },
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
            }

            // Footer fijo con botones
            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Cancelar
                OutlinedButton(
                    onClick = {
                        addUserViewModel.resetForm()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading
                ) {
                    Text("Cancelar")
                }

                // Botón Agregar
                Button(
                    onClick = {
                        if (addUserViewModel.validateForm(existingUsers)) {
                            addUserViewModel.setLoading(true)

                            // Llamar a API si usersViewModel está disponible
                            if (usersViewModel != null) {
                                usersViewModel.createNewUser(
                                    username = state.username,
                                    email = state.email,
                                    nombre = state.nombre,
                                    apellido = state.apellido,
                                    password = state.password,
                                    roles = state.selectedRoles.map { role ->
                                        // Convertir a formato API si es necesario
                                        when (role) {
                                            "Administrador" -> "ROLE_ADMINISTRADOR"
                                            "Auditor" -> "ROLE_AUDITOR"
                                            "Compras" -> "ROLE_COMPRAS"
                                            "Ventas" -> "ROLE_VENTAS"
                                            "Supervisor" -> "ROLE_SUPERVISOR"
                                            "Empleado" -> "ROLE_EMPLEADO"
                                            "Gerencia" -> "ROLE_GERENCIA"
                                            "Inventario" -> "ROLE_INVENTARIO"
                                            "Logistica" -> "ROLE_LOGISTICA"
                                            "Produccion" -> "ROLE_PRODUCCION"
                                            "Trabajador" -> "ROLE_TRABAJADOR"
                                            else -> role
                                        }
                                    }.toList(),
                                    onSuccess = {
                                        addUserViewModel.resetForm()
                                        onDismiss()
                                    },
                                    onError = { error ->
                                        errorMessage = error
                                        addUserViewModel.setLoading(false)
                                    }
                                )
                            } else {
                                // Fallback: crear usuario localmente (sin API)
                                val newUser = User(
                                    id = UUID.randomUUID().toString(),
                                    username = state.username,
                                    nombre = state.nombre,
                                    apellido = state.apellido,
                                    email = state.email,
                                    activo = true,
                                    roles = state.selectedRoles.toList()
                                )
                                onUserAdded(newUser)
                                addUserViewModel.resetForm()
                            }
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
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

@Composable
private fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // Placeholder para BackHandler - requiere importar androidx.activity.compose.BackHandler
}

private fun formatRolDisplayName(role: String): String {
    return when (role) {
        "ROLE_ADMINISTRADOR" -> "Administrador"
        "ROLE_AUDITOR" -> "Auditor"
        "ROLE_COMPRAS" -> "Compras"
        "ROLE_VENTAS" -> "Ventas"
        "ROLE_SUPERVISOR" -> "Supervisor"
        "ROLE_EMPLEADO" -> "Empleado"
        "ROLE_GERENCIA" -> "Gerencia"
        "ROLE_INVENTARIO" -> "Inventario"
        "ROLE_LOGISTICA" -> "Logística"
        "ROLE_PRODUCCION" -> "Producción"
        "ROLE_TRABAJADOR" -> "Trabajador"
        else -> role.removePrefix("ROLE_")
            .replace("_", " ")
            .lowercase()
            .replaceFirstChar { it.uppercase() }
    }
}

