package cl.duoc.maestranza_v2.ui.screens.movimientos

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementsScreenExpanded(
    navController: NavController,
    viewModel: MainViewModel
) {
    cl.duoc.maestranza_v2.ui.components.ScaffoldWrapper(
        navController = navController,
        showDrawer = true,
        title = "Movimientos"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Pantalla de Movimientos",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Contenido próximamente...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@SuppressLint("ComposableNaming", "ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "Expanded",
    device = "spec:width=1280dp,height=800dp,dpi=420"
)
@Composable
fun MovementsScreenExpandedPreview() {
    Maestranza_V2Theme {
        MovementsScreenExpanded(
            navController = rememberNavController(),
            viewModel = MainViewModel()
        )
    }
}

