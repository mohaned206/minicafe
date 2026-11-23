package com.example.minicafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.minicafe.ui.AppViewModel
import com.example.minicafe.ui.screens.CartScreen
import com.example.minicafe.ui.screens.DetailScreen
import com.example.minicafe.ui.screens.HomeScreen
import com.example.minicafe.ui.screens.LoginScreen
import com.example.minicafe.ui.screens.OnboardingScreen
import com.example.minicafe.ui.theme.MiniCafeTheme

object Routes {
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val HOME = "home"
    const val CART = "cart"
    const val DETAIL = "detail/{id}"
    fun detail(id: Int) = "detail/$id"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniCafeTheme { AppNav() }
        }
    }
}

@Composable
fun AppNav() {
    val nav = rememberNavController()
    // Un seul ViewModel partagé (panier, recherche...) pour toute l'app
    val vm: AppViewModel = viewModel()

    NavHost(navController = nav, startDestination = Routes.ONBOARDING) {

        composable(Routes.ONBOARDING) {
            OnboardingScreen(onStart = { nav.navigate(Routes.LOGIN) })
        }

        composable(Routes.LOGIN) {
            LoginScreen(onLogin = {
                nav.navigate(Routes.HOME) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            })
        }

        composable(Routes.HOME) {
            HomeScreen(
                vm = vm,
                onCoffeeClick = { nav.navigate(Routes.detail(it)) },
                onOpenCart = { nav.navigate(Routes.CART) },
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
        ) { entry ->
            DetailScreen(
                vm = vm,
                coffeeId = entry.arguments?.getInt("id") ?: 1,
                onBack = { nav.popBackStack() },
                onOpenCart = { nav.navigate(Routes.CART) },
            )
        }

        composable(Routes.CART) {
            CartScreen(
                vm = vm,
                onBack = { nav.popBackStack() },
                onBrowse = { nav.popBackStack(Routes.HOME, inclusive = false) },
                onOrderDone = { nav.popBackStack(Routes.HOME, inclusive = false) },
            )
        }
    }
}
