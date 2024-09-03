package com.khamdan.todo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.khamdan.todo.screen.ChecklistScreen
import com.khamdan.todo.screen.LoginScreen
import com.khamdan.todo.screen.RegisterScreen


@Composable
fun TodoApp(viewModel: ChecklistViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (viewModel.isLogin()) "checklist" else "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                navigateRegisterPage = { navController.navigate("register") }
            ) {
                navController.navigate("checklist") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("register") {
            RegisterScreen(viewModel) {
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            }
        }
        composable("checklist") { ChecklistScreen(viewModel) }
    }
}
