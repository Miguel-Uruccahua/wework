package com.example.wework.ui.login

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.composeuisuite.ohteepee.OhTeePeeInput
import com.composeuisuite.ohteepee.configuration.OhTeePeeCellConfiguration
import com.composeuisuite.ohteepee.configuration.OhTeePeeConfigurations
import com.example.wework.ui.navigation.Login
import com.example.wework.ui.navigation.TopBar


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginWithPhoneScreen(
    navHostController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity
    val errorMessage: String by loginViewModel.errorMessage.observeAsState(initial = "")
    val numberPhone: String by loginViewModel.numberPhone.observeAsState(initial = "")
    var showInputNumber: Boolean by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = numberPhone,
            onValueChange = { loginViewModel.onChangeNumberPhone(it) },
            modifier = Modifier.padding(start = 32.dp, end = 32.dp),
            label = { Text(text = "Ingrese su Número de telefono") },
        )
        Spacer(modifier = Modifier.size(5.dp))
        Button(onClick = {
            loginViewModel.loginWithPhone(numberPhone, activity,
                onCodeSent = { showInputNumber = true },
                onVerificationComplete = { navHostController.navigate(TopBar) })
        }) { Text(text = "Enviar Codigo") }
        Button(onClick = { navHostController.navigate(Login) }
        ) { Text(text = "Regresar") }
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage)
        }
        Spacer(modifier = Modifier.size(5.dp))
        if (showInputNumber) {
            InputCodeNumber() {
                loginViewModel.verifyCode(it) { navHostController.navigate(TopBar) }
                showInputNumber = false
            }
        }
    }
}

@Composable
fun InputCodeNumber(
    onComplete: (String) -> Unit
) {
    var otpValue: String by remember { mutableStateOf("") }

    val defaultCellConfig = OhTeePeeCellConfiguration.withDefaults(
        borderColor = Color.LightGray,
        borderWidth = 1.dp,
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(
            color = Color.Black
        )
    )
    OhTeePeeInput(
        value = otpValue,
        onValueChange = { newValue, isValid ->
            otpValue = newValue
            if (otpValue.length == 6 && !otpValue.contains(" ")) {
                onComplete(otpValue)
            }
        },
        configurations = OhTeePeeConfigurations.withDefaults(
            cellsCount = 6,
            emptyCellConfig = defaultCellConfig,
            cellModifier = Modifier
                .padding(horizontal = 4.dp)
                .size(48.dp),
        ),
    )
}