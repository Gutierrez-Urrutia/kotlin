package cl.duoc.maestranza_v2.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.maestranza_v2.R
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreenMedium() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Medium") })
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la app",
                modifier = Modifier.weight(1f).sizeIn(maxWidth = 200.dp)
            )
            Spacer(modifier = Modifier.width(32.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { /* Acción futura */ },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Comenzar")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Medium", device = "spec:width=800dp,height=1280dp,dpi=320" )
@Composable
fun WelcomeScreenMediumPreview(){
    Maestranza_V2Theme {
        WelcomeScreenMedium()
    }
}