package cl.duoc.maestranza_v2.ui.screens.users

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
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
import cl.duoc.maestranza_v2.ui.screens.inventory.InventoryScreenCompact
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

data class UserItem(
    val username: String,
    val name: String,
    val email: String,
    val roles: String,
    val status: String
)

val dummyUserItems = listOf(
    UserItem("pablo.a", "Pablo Alarcón", "pablo.a@duoc.cl", "Admin", "Activo"),
    UserItem("ana.m", "Ana María Soto", "ana.m@duoc.cl", "Empleado", "Activo"),
    UserItem("carlos.g", "Carlos Gómez", "carlos.g@duoc.cl", "Supervisor", "Inactivo"),
    UserItem("laura.f", "Laura Fuentes", "laura.f@duoc.cl", "Empleado", "Activo"),
    UserItem("miguel.r", "Miguel Rojas", "miguel.r@duoc.cl", "Admin", "Activo"),
    UserItem("sofia.v", "Sofía Valdés", "sofia.v@duoc.cl", "Empleado", "Activo"),
    UserItem("diego.c", "Diego Castro", "diego.c@duoc.cl", "Supervisor", "Activo"),
    UserItem("javiera.h", "Javiera Herrera", "javiera.h@duoc.cl", "Empleado", "Inactivo")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreenCompact(
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
                navigationIcon = {
                    IconButton(onClick = { /* Acción futura */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción futura */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Opciones de Usuario")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            // Usamos el ViewModel para navegar, igual que en la guía
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
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar usuario por nombre o email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                trailingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Tabla de usuarios
            Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                LazyColumn(modifier = Modifier.width(500.dp)) { // Ancho ajustado para compact
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("Usuario", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp))
                            Text("Nombre", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(200.dp)
                                .padding(8.dp))
                            Text("Estado", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(180.dp)
                                .padding(8.dp))
                        }
                        Divider()
                    }
                    items(dummyUserItems) { user ->
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(user.username, modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp))
                            Text(user.name, modifier = Modifier
                                .width(200.dp)
                                .padding(8.dp))
                            Text(user.status, modifier = Modifier
                                .width(180.dp)
                                .padding(8.dp))
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "User Compact",
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun UserScreenCompactPreview() {
    Maestranza_V2Theme {
        UserScreenCompact(
            navController = rememberNavController(),
            viewModel = MainViewModel()
        )
    }
}

