package com.example.wework.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wework.ui.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val menuNavController = rememberNavController()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                contentDrawer(navController, menuNavController,
                    oncloseDrawer = { scope.launch { drawerState.close() } })
            }
        },
    ) {
        Scaffold(floatingActionButton = {
            ExtendedFloatingActionButton(text = { Text("Menu") },
                icon = { Icon(Icons.Filled.Menu, contentDescription = "") },
                onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                })
        }) { contentPadding ->
            SecondNavigation(menuNavController)
        }
    }
}

@Composable
fun contentDrawer(
    navController: NavHostController,
    secondNavigation: NavHostController ,
    oncloseDrawer: ()->Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val userData = mainViewModel.userInfo.collectAsState("")

    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = rememberVectorPainter(image = Icons.Default.Face),
            contentDescription = "Imagen de perfil",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Text(text = userData.value)

        Spacer(modifier = Modifier.size(10.dp))

        buttomModel(Icons.Filled.Email, "Mensajes") {
            secondNavigation.navigate(Home)
            oncloseDrawer()
        }

        if (userData.value != "Anónimo") {
            buttomModel(Icons.Filled.AccountCircle, "Contactos") {
                secondNavigation.navigate(Detail)
                oncloseDrawer()
            }
        }

        buttomModel(Icons.Filled.Settings, "Configuración") {
            secondNavigation.navigate(Detail)
            oncloseDrawer()
        }

        buttomModel(Icons.Filled.ExitToApp, "Salir") {
            mainViewModel.logout()
            navController.navigate(Login)
        }

    }
}

@Composable
fun buttomModel(icon: ImageVector, text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF172C42)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(icon, contentDescription = "", Modifier.padding(end = 10.dp))
            Text(text = text)
        }
    }
}