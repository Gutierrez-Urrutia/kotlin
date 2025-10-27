package cl.duoc.maestranza_v2.ui.screens.welcome

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.R
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreenExpanded(
    navController: NavController,
    viewModel: MainViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Expanded") })
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la app",
                modifier = Modifier
                    .weight(1f)
                    .sizeIn(maxWidth = 250.dp)
            )
            Spacer(modifier = Modifier.width(48.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { /* Acción futura */ },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Comenzar")
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Expanded", device = "spec:width=1280dp,height=800dp,dpi=240" )
@Composable
fun WelcomeScreenExpandedPreview(){
    Maestranza_V2Theme {
        WelcomeScreenExpanded(
            navController = rememberNavController(),
            viewModel = MainViewModel()
        )
    }
}