package cl.duoc.maestranza_v2.ui.screens.users

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.navigation.Screen
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreenExpanded(
    navController: NavController,
    viewModel: MainViewModel
) {
    var searchText by remember { mutableStateOf("") }
    val items = listOf(Screen.Inventory, Screen.Users)
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios") },
                navigationIcon = { IconButton(onClick = { /* Acción futura */ }) { Icon(Icons.Default.Menu, "Menú") } },
                actions = { IconButton(onClick = { /* Acción futura */ }) { Icon(Icons.Default.Notifications, "Notificaciones") } }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            viewModel.navigateTo(screen)
                        },
                        label = { Text(text = screen.route.replaceFirstChar { it.uppercase() }) },
                        icon = {
                            Icon(
                                imageVector = if (screen == Screen.Inventory) Icons.Default.Inventory else Icons.Default.Person,
                                contentDescription = screen.route
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar usuario por nombre, email o rol") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                trailingIcon = { Icon(Icons.Default.Search, "Buscar") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Tabla de usuarios
            LazyColumn {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Usuario", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(1.5f)
                            .padding(12.dp))
                        Text("Nombre", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(2.5f)
                            .padding(12.dp))
                        Text("Email", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(2.5f)
                            .padding(12.dp))
                        Text("Roles", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(1.5f)
                            .padding(12.dp))
                        Text("Estado", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(1f)
                            .padding(12.dp))
                        Text("Acciones", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(1f)
                            .padding(12.dp))
                    }
                    Divider()
                }
                items(dummyUserItems) { user ->
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(user.username, modifier = Modifier
                            .weight(1.5f)
                            .padding(12.dp))
                        Text(user.name, modifier = Modifier
                            .weight(2.5f)
                            .padding(12.dp))
                        Text(user.email, modifier = Modifier
                            .weight(2.5f)
                            .padding(12.dp))
                        Text(user.roles, modifier = Modifier
                            .weight(1.5f)
                            .padding(12.dp))
                        Text(user.status, modifier = Modifier
                            .weight(1f)
                            .padding(12.dp))
                        IconButton(onClick = { /* Acción futura */ }, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar usuario")
                        }
                    }
                    Divider()
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "User Expanded",
    device = "spec:width=1280dp,height=800dp,dpi=320"
)
@Composable
fun UserScreenExpandedPreview() {
    Maestranza_V2Theme {
        UserScreenExpanded(
            navController = rememberNavController(),
            viewModel = MainViewModel()
        )
    }
}