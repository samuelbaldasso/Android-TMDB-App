package com.sbaldasso.tmdbapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sbaldasso.tmdbapp.presentation.screen.details.DetailsScreen
import com.sbaldasso.tmdbapp.presentation.screen.home.HomeScreen
import com.sbaldasso.tmdbapp.presentation.screen.search.SearchScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Details.createRoute(movieId))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onBackClick = { navController.navigateUp() },
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Details.createRoute(movieId))
                }
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) {
            DetailsScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}