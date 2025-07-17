package com.chintanrathod.pokemon.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chintanrathod.domain.models.detail.PokemonDetailData
import com.chintanrathod.pokemon.viewmodel.PokemonViewModel

/**
 * Displays the Pokemon detail inside a Scaffold.
 *
 * @param pokemonId The id of the pokemon selected from the list
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel : PokemonViewModel,
) {

    val detail by viewModel.pokemonDetail.collectAsState()

    // Trigger details loading when screen appears
    LaunchedEffect(pokemonId) {
        viewModel.loadPokemonDetail(pokemonId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "Pokemon Detail") })
        }
    ) { padding ->

        detail?.let {
            PokemonDetailView(
                topPadding = padding.calculateTopPadding(),
                item = it
            )
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
fun PokemonDetailView(
    topPadding: Dp,
    item: PokemonDetailData,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = topPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.image)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id= com.chintanrathod.pokemon.R.drawable.ic_launcher_background),
            contentDescription = stringResource(com.chintanrathod.pokemon.R.string.app_name),
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(200.dp),
        )

        Text(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .testTag("PokemonDetailName"),
            text = item.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp),
            text = "Height is: " + item.height,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp),
            text = "Weight is: " + item.weight,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp),
            text = "Base Experience is: " + item.baseExperience,
            style = MaterialTheme.typography.titleLarge
        )
    }
}