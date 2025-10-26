package cl.duoc.maestranza_v2.ui.screens.inventory

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
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
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme

// --- Modelo de Datos y Datos de Prueba ---
data class InventoryItem(
    val code: String,
    val name: String,
    val category: String,
    val stock: Int
)

val dummyInventoryItems = listOf(
    InventoryItem("HCOR-001", "Broca HSS 5mm", "Herramientas de Corte", 50),
    InventoryItem("HCOR-002", "Broca HSS 10mm", "Herramientas de Corte", 35),
    InventoryItem("HCOR-003", "Fresa frontal 25mm", "Herramientas de Corte", 15),
    InventoryItem("HSUJ-001", "Prensa de banco 4\"", "Herramientas de Sujeción", 20),
    InventoryItem("HMEC-001", "Calibre digital 150mm", "Herramientas de Medición", 40),
    InventoryItem("HCOR-004", "Sierra circular 200mm", "Herramientas de Corte", 25),
    InventoryItem("HMEC-002", "Micrómetro 0-25mm", "Herramientas de Medición", 18),
    InventoryItem("HCOR-005", "Cuchilla de torno TNMG", "Herramientas de Corte", 120),
)
// -----------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenCompact() {
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario") },
                navigationIcon = {
                    IconButton(onClick = { /* Acción futura */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción futura */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                    }
                }
            )
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
                label = { Text("Buscar en inventario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                trailingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                LazyColumn(modifier = Modifier.width(500.dp)) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("Código", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp))
                            Text("Nombre", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(200.dp)
                                .padding(8.dp))
                            Text("Categoría", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(180.dp)
                                .padding(8.dp))
                        }
                        Divider()
                    }
                    items(dummyInventoryItems) { item ->
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(item.code, modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp))
                            Text(item.name, modifier = Modifier
                                .width(200.dp)
                                .padding(8.dp))
                            Text(item.category, modifier = Modifier
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

@Preview(
    showBackground = true,
    name = "Compacto (Teléfono)",
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun InventoryScreenCompactPreview() {
    Maestranza_V2Theme {
        InventoryScreenCompact()
    }
}
