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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenMedium() {
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario") },
                navigationIcon = { IconButton(onClick = { /* Acción futura */ }) { Icon(Icons.Default.Menu, "Menú") } },
                actions = { IconButton(onClick = { /* Acción futura */ }) { Icon(Icons.Default.Notifications, "Notificaciones") } }
            )
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
                label = { Text("Buscar en inventario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                trailingIcon = { Icon(Icons.Default.Search, "Buscar") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                LazyColumn(modifier = Modifier.width(700.dp)) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("Código", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp))
                            Text("Nombre", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(250.dp)
                                .padding(8.dp))
                            Text("Categoría", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(230.dp)
                                .padding(8.dp))
                            Text("Stock", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(100.dp)
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
                                .width(250.dp)
                                .padding(8.dp))
                            Text(item.category, modifier = Modifier
                                .width(230.dp)
                                .padding(8.dp))
                            Text(item.stock.toString(), modifier = Modifier
                                .width(100.dp)
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
    name = "Mediano (Tablet Vertical)",
    device = "spec:width=800dp,height=1280dp,dpi=320"
)
@Composable
fun InventoryScreenMediumPreview() {
    Maestranza_V2Theme {
        InventoryScreenMedium()
    }
}