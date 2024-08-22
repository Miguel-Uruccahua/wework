package com.example.wework.ui.signup

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wework.ui.navigation.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavHostController, signupViewModel: SignupViewModel= hiltViewModel()) {

    var errorMessage: String by remember { mutableStateOf("") }
    val user: String by signupViewModel.user.observeAsState(initial = "")
    val password: String by signupViewModel.password.observeAsState(initial = "")

    Column(modifier = Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = user,
            onValueChange = { signupViewModel.onChangeUser(it) },
            modifier = Modifier.padding(start = 32.dp, end = 32.dp),
            label = { Text(text = "User or Email") },
        )
        OutlinedTextField(
            value = password,
            onValueChange = { signupViewModel.onChangePassword(it) },
            modifier = Modifier.padding(start = 32.dp, end = 32.dp),
            label = { Text(text = "Password") },
        )
        Button(onClick = { signupViewModel.register( navigateToHome = {
                navController.navigate(TopBar)
            },showError = {
                errorMessage = it
            })
        }) { Text(text = "Registrar") }

        if(errorMessage.isNotEmpty()){ Text(text = errorMessage)}
    }

}