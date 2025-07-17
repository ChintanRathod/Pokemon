package com.chintanrathod.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chintanrathod.pokemon.ui.PokemonDetailScreen
import com.chintanrathod.pokemon.ui.PokemonListScreen
import com.chintanrathod.pokemon.ui.theme.PokemonTheme
import com.chintanrathod.pokemon.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * [MainActivity] is responsible for hosting Toolbar and Pokemon List and Details screen.
 *
 * It will observe [com.chintanrathod.pokemon.viewmodel.PokemonViewModel.pokemonList]
 *
 * @author ChintanRathod
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigation()
                }
            }
        }
    }

    /**
     * Sets up navigation graph for the app using Jetpack Navigation Compose.
     * Displays the [PokemonListScreen] as the start destination and connects the ViewModel.
     */
    @Composable
    fun MainNavigation() {

        val navHostController = rememberNavController()
        val viewModel : PokemonViewModel = hiltViewModel()

        // get pokemon list here from view model
        val pokemonList by viewModel.pokemonList.collectAsState()
        val searchResults by viewModel.searchResults.collectAsState()
        var searchQuery = viewModel.searchQuery

        NavHost(navController = navHostController, startDestination = "PokemonList") {
            composable(route = "PokemonList") {
                PokemonListScreen(
                    pokemonList = pokemonList,
                    searchQuery = searchQuery,
                    viewModel = viewModel,
                    searchResults = searchResults,
                    onPokemonSelect = { pokemonId ->
                        navHostController.navigate("PokemonDetail/$pokemonId")
                    }
                )
            }

            composable(
                route = "PokemonDetail/{pokemonId}",
                arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
            ) { backStackEntry ->
                val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: return@composable

                PokemonDetailScreen(
                    pokemonId = pokemonId,
                    viewModel = viewModel
                )
            }
        }
    }
}