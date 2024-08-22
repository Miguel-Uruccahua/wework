
package com.example.wework.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wework.ui.MainViewModel
import com.example.wework.ui.detail.DetailScreen
import com.example.wework.ui.home.HomeScreen
import com.example.wework.ui.login.LoginScreen
import com.example.wework.ui.login.LoginWithPhoneScreen
import com.example.wework.ui.signup.SignupScreen

@Composable
fun MainNavigation(mainViewModel:MainViewModel= hiltViewModel()) {
    var logged = false
    mainViewModel.checkLogged { logged = it  }
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = if(!logged)Login else (TopBar)) {
        composable<Login> {
            LoginScreen(navController)
        }
        composable<Signup> {
            SignupScreen(navController)
        }
        composable<TopBar> {
            TopBar(navController)
        }
        composable<LoginWithPhone> {
            LoginWithPhoneScreen(navController)
        }
    }

}

@Composable
fun SecondNavigation(navController:NavHostController){
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(navController)
        }
        composable<Detail> {
            DetailScreen(navController)
        }
    }
}

