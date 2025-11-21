package cl.duoc.maestranza_v2.ui.screens.user

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
import cl.duoc.maestranza_v2.ui.screens.users.UserScreenExpanded
import cl.duoc.maestranza_v2.ui.screens.users.dummyUserItems
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreenMedium(
    navController: NavController,
    viewModel: MainViewModel
) {
    val items = listOf(Screen.Inventory, Screen.Users)
    var selectedItem by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios") },
                navigationIcon = { IconButton(onClick = { /* Acción futura */ }) { Icon(Icons.Default.Menu, "Menú") } },
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
                .padding(horizontal = 24.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar usuario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                trailingIcon = { Icon(Icons.Default.Search, "Buscar") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Tabla de usuarios
            Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                LazyColumn(modifier = Modifier.width(700.dp)) { // Ancho total ajustado para medium
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("Usuario", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(100.dp)
                                .padding(8.dp))
                            Text("Nombre", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(180.dp)
                                .padding(8.dp))
                            Text("Email", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(180.dp)
                                .padding(8.dp))
                            Text("Roles", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp))
                            Text("Estado", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp))
                        }
                        Divider()
                    }
                    items(dummyUserItems) { user ->
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(user.username, modifier = Modifier
                                .width(100.dp)
                                .padding(8.dp))
                            Text(user.name, modifier = Modifier
                                .width(180.dp)
                                .padding(8.dp))
                            Text(user.email, modifier = Modifier
                                .width(180.dp)
                                .padding(8.dp))
                            Text(user.roles, modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp))
                            Text(user.status, modifier = Modifier
                                .width(120.dp)
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