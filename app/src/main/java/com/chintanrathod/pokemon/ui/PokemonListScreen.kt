package com.chintanrathod.pokemon.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.chintanrathod.domain.models.browse.PokemonListItem

/**
 * Displays the Pokemon list using a lazy column inside a Scaffold.
 *
 * @param pokemonList The list of [PokemonListItem] to display in the UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    pokemonList: List<PokemonListItem>,
    onPokemonSelect:(Int) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "Pokemon List") })
        }
    ) { padding ->

        /*
        Instead of column, needs to take LazyColum so that
        it can have scrollable view with lazy loading of items
        with respect to phone visible contents
         */
        LazyColumn (modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            items(pokemonList.size) { index ->
                val item = pokemonList[index]

                ListItemView(
                    index = index.inc(),
                    item = item,
                    onPokemonSelect = onPokemonSelect
                )
                HorizontalDivider(
                    color = Gray
                )
            }
        }
    }
}

/**
 * Displays a single item in the Pokemon list.
 *
 * @param index The index of the item.
 * @param item The Pokemon list item containing name and URL.
 */
@Composable
fun ListItemView(
    index: Int,
    item: PokemonListItem,
    onPokemonSelect:(Int) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable {
                val id = item.url.trimEnd('/').substringAfterLast('/').toInt()
                onPokemonSelect(id)
            }
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("PokemonItem")
    ) {
        Text(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp),
            text = index.toString() + ". " + item.name,
            style = MaterialTheme.typography.titleLarge
        )
    }
}