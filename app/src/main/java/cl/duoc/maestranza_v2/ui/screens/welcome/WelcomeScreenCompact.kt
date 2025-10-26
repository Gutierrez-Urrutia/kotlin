package cl.duoc.maestranza_v2.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.maestranza_v2.R
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreenCompact() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Compact") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la app",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { /* Acción futura */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comenzar")
            }
        }
    }
}

@Preview(showBackground = true, name = "Compact", widthDp = 320, heightDp = 640 )
@Composable
fun WelcomeScreenCompactPreview(){
    Maestranza_V2Theme {
        WelcomeScreenCompact()
    }
}
