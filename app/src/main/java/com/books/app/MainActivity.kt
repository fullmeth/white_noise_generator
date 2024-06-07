package com.books.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.books.app.common.Destination
import com.books.app.details.DetailsScreen
import com.books.app.details.DetailsViewModel
import com.books.app.details.bookDetails
import com.books.app.home.HomeScreen
import com.books.app.home.HomeViewModel
import com.books.app.splash.SplashScreen
import com.books.app.splash.SplashViewModel
import com.books.app.ui.theme.TestTaskBookAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainNavController = rememberNavController()
            TestTaskBookAppTheme {
                TransparentSystemBars()
                NavHost(
                    navController = mainNavController,
                    startDestination = Destination.Splash.route
                ) {
                    composable(
                        route = Destination.Splash.route
                    ) {
                        val viewModel = koinViewModel<SplashViewModel>()
                        val state by viewModel.splashUiState.collectAsState()
                        state.navigate?.invoke()?.let {
                            mainNavController.navigate(Destination.Home.route) {
                                popUpTo(Destination.Splash.route) {
                                    inclusive = true
                                }
                            }
                        }
                        SplashScreen()
                    }
                    composable(
                        route = Destination.Home.route
                    ) {
                        val viewModel = koinViewModel<HomeViewModel>()
                        val state by viewModel.homeUiState.collectAsState()
                        HomeScreen(
                            state = state,
                            onBookClicked = { mainNavController.bookDetails(it) }
                        )
                    }
                    composable(
                        route = Destination.Details.route
                    ) {
                        val viewModel = koinViewModel<DetailsViewModel>()
                        val state by viewModel.detailsUiState.collectAsState()
                        DetailsScreen(
                            state = state,
                            onBookClicked = { mainNavController.bookDetails(it) })
                    }
                }
            }
        }
    }
}

@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false
        )

        onDispose {}
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestTaskBookAppTheme {
        SplashScreen()
    }
}