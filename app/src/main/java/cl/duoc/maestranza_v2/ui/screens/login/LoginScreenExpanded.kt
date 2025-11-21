package cl.duoc.maestranza_v2.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.R
import cl.duoc.maestranza_v2.navigation.Screen
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@Composable
fun LoginScreenExpanded(navController: NavController, viewModel: MainViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la empresa",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Maestranza Unidos", style = MaterialTheme.typography.headlineMedium)
            Text("Inicio de Sesión", style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(modifier = Modifier.width(32.dp))
        Card(
            modifier = Modifier.weight(1f)
                .padding(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { viewModel.navigateTo(Screen.Inventory) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ingresar")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("O inicia sesión con tu huella digital")
            }
        }
    }
}