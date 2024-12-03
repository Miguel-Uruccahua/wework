package com.example.wework.ui.login

import android.app.Activity
import android.widget.ProgressBar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wework.R
import com.example.wework.ui.navigation.LoginWithPhone
import com.example.wework.ui.navigation.Signup
import com.example.wework.ui.navigation.TopBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException


@Composable
fun LoginScreen(
    navHostController: NavHostController, loginViewModel: LoginViewModel = hiltViewModel()
) {
    val isLoading: Boolean by loginViewModel.isLoading.observeAsState(initial = false)

    Column(modifier = Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp),
                color = MaterialTheme.colorScheme.secondary,
                strokeWidth = 10.dp
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(text = "Cargando...")

        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Spacer(modifier = Modifier.size(10.dp))

                contentLogin(navHostController)

                Spacer(modifier = Modifier.size(10.dp))

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun contentLogin(navHostController: NavHostController,loginViewModel: LoginViewModel= hiltViewModel()){

    var errorMessage: String by remember { mutableStateOf("") }
    val activity = LocalContext.current as Activity
    val user: String by loginViewModel.user.observeAsState(initial = "")
    val password: String by loginViewModel.password.observeAsState(initial = "")
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    loginViewModel.loginWithGoogle(account.idToken!!) {
                        navHostController.navigate(
                            TopBar
                        )
                    }
                } catch (e: ApiException) {
                    errorMessage = e.message.toString()
                }
            }
        }

    Column(  verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = user,
            onValueChange = { loginViewModel.onChangeUser(it) },
            modifier = Modifier.padding(start = 32.dp, end = 32.dp),
            label = { Text(text = "User or Email") },
        )

        OutlinedTextField(
            value = password,
            onValueChange = { loginViewModel.onChangePassword(it) },
            modifier = Modifier.padding(start = 32.dp, end = 32.dp),
            label = { Text(text = "Password") },
        )

        Spacer(modifier = Modifier.size(10.dp))

        Row(
            Modifier
                .padding(start = 50.dp, end = 50.dp)
                .fillMaxWidth(),Arrangement.Center,Alignment.CenterVertically) {
            Button(modifier = Modifier.weight(0.5f),
                onClick = {
                    loginViewModel.onLoginSelected(LoginType.Email, activity,
                        navigateToHome = { navHostController.navigate(TopBar) },
                        showError = { errorMessage = it })
                    loginViewModel.sendData()
                }) { Text(text = "Ingresar") }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                modifier = Modifier.weight(0.5f),
                onClick = { navHostController.navigate(Signup)
                }) { Text(text = "Registrar") }
        }

        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Ingresar Con: ")
        Spacer(modifier = Modifier.size(5.dp))

        // START LOGIN WITH PHONE - GOOGLE - GITHUB
        Row (
            Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, end = 50.dp)){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(modifier = Modifier.size(60.dp), onClick = {
                    navHostController.navigate(LoginWithPhone)
                }) { Icon(Icons.Default.Phone, "" , Modifier.size(30.dp) ) }
                Text(text = "Celular")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(modifier = Modifier.size(60.dp), onClick = {
                    loginViewModel.getGoogleClient { launcher.launch(it.signInIntent) }
                }) { Icon(painterResource(id = R.drawable.ic_google), "" , Modifier.size(30.dp) ) }
                Text(text = "Google")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(modifier = Modifier.size(60.dp), onClick = {
                    loginViewModel.onLoginSelected(LoginType.Github,
                        activity,
                        navigateToHome = { navHostController.navigate(TopBar) },
                        showError = { errorMessage = it })
                }) { Icon(painterResource(id = R.drawable.ic_github),"", Modifier.size(30.dp)  ) }
                Text(text = "GitHub")
            }
        }
        // END LOGIN WITH PHONE - GOOGLE - GITHUB

        Spacer(modifier = Modifier.size(10.dp))

        //START LOGIN WITH MICROSOFT - TWITTER - ANONYMOUSLY
        Row (
            Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, end = 50.dp)){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(modifier = Modifier.size(60.dp), onClick = {
                    loginViewModel.onLoginSelected(LoginType.Microsoft,
                        activity,
                        navigateToHome = { navHostController.navigate(TopBar) },
                        showError = { errorMessage = it })
                }) { Icon(painterResource(id = R.drawable.ic_microsoft), "" , Modifier.size(30.dp) ) }
                Text(text = "Microsoft")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(modifier = Modifier.size(60.dp), onClick = {
                    loginViewModel.onLoginSelected(LoginType.Twitter,
                        activity,
                        navigateToHome = { navHostController.navigate(TopBar) },
                        showError = { errorMessage = it })
                }) { Icon(painterResource(id = R.drawable.ic_twitter), "" , Modifier.size(30.dp) ) }
                Text(text = "Twitter")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(modifier = Modifier.size(60.dp), onClick = {
                    loginViewModel.onLoginSelected(LoginType.Anonymously,
                        activity,
                        navigateToHome = { navHostController.navigate(TopBar) },
                        showError = { errorMessage = it })
                }) { Icon(painterResource(id = R.drawable.ic_anonymously), "" , Modifier.size(30.dp) )}
                Text(text = "Anonimo")
            }
        }
        //END LOGIN WITH MICROSOFT - TWITTER - ANONYMOUSLY

        if (errorMessage.isNotEmpty()) { Text(text = errorMessage) }
    }

}