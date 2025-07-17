package com.chintanrathod.pokemon.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chintanrathod.domain.common.Resource
import com.chintanrathod.domain.models.browse.PokemonListData
import com.chintanrathod.domain.models.browse.PokemonListItem
import com.chintanrathod.domain.models.detail.PokemonDetailData
import com.chintanrathod.domain.usecase.GetPokemonDetailUseCase
import com.chintanrathod.domain.usecase.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

/**
 * [PokemonViewModel] is responsible for handling the UI logic of the Pokemon screen.
 *
 * It interacts with the [GetPokemonListUseCase] to fetch Pokemon data and exposes
 * the data to the UI using StateFlow.
 *
 * @property getPokemonListUseCase The use case used to fetch Pokemon list.
 * @property ioDispatcher The Coroutine Dispatcher helpful for Unit test.
 *
 * @author ChintanRathod
 */
@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    /*
    Following object will store the initial and then next url to call.
    I.e.
    https://pokeapi.co/api/v2/pokemon
    https://pokeapi.co/api/v2/pokemon?offset=20&limit=20
     */
    private var pokemonListUrl = "https://pokeapi.co/api/v2/pokemon"

    /*
    Store list of pokemon in the list.
     */
    private val _pokemonList = MutableStateFlow<List<PokemonListItem>>(emptyList())
    val pokemonList: StateFlow<List<PokemonListItem>> = _pokemonList.asStateFlow()

    /*
    Store detail of pokemon.
     */
    private val _pokemonDetail = MutableStateFlow<PokemonDetailData?>(null)
    val pokemonDetail: StateFlow<PokemonDetailData?> = _pokemonDetail.asStateFlow()

    /*
    This object helps store temporary search query
     */
    var searchQuery by mutableStateOf(TextFieldValue("", TextRange(0)))
        private set

    private val _searchResults = MutableStateFlow<List<PokemonListItem>>(emptyList())
    val searchResults: StateFlow<List<PokemonListItem>> = _searchResults

    /*
    This Job will be a coroutine which do Bounce effect for search with some delays
    This helps to allow user to type meaningful word before we bombarding on search results
    This also helps to lower the queries and processing and displaying limited searches
     */
    private var searchJob: Job? = null

    /*
    we need to fetch initial list of pokemon when application launched
    and viewmodel is initialised
     */
    init {
        // Fetch initial list from API
        loadPokemonList(url = pokemonListUrl)
    }

    /**
    Method to call use case with list URL [pokemonListUrl]
     */
    fun loadPokemonList(url: String = pokemonListUrl) {
        viewModelScope.launch (ioDispatcher) {
            handleResponse(getPokemonListUseCase(url))
        }
    }

    /**
     * Method to handle the response of the [getPokemonListUseCase]
     *
     * input: [Resource<PokemonListData>]
     */
    private fun handleResponse(result: Resource<PokemonListData>) {
        when (result) {
            /*
            Check for error
             */
            is Resource.Error -> {
                // Display error
            }
            /*
            Check for success and assign the data to _pokemonList
            Also assign next URL to pokemonListUrl
             */
            is Resource.Success -> {
                result.data?.let { data ->
                    data.results?.let { results ->
                        results.map {
                            if (!pokemonList.value.contains(it)) {
                                _pokemonList.value = _pokemonList.value + it
                            }
                        }
                    }
                    data.next?.let {
                        pokemonListUrl = it
                    }
                }
            }
            /*
            Check for loading state
             */
            is Resource.Loading -> {
                // Handle loading state
            }
        }
    }

    /**
    Method to call use case with pokemon Id [pokemonListUrl]
     */
    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch (ioDispatcher) {
            handleDetailResponse(getPokemonDetailUseCase(id = id))
        }
    }

    /**
     * Method to handle the response of the [getPokemonDetailUseCase]
     *
     * input: [Resource<PokemonDetailData>]
     */
    private fun handleDetailResponse(result: Resource<PokemonDetailData>) {
        when (result) {
            /*
            Check for error
             */
            is Resource.Error -> {
                // Display error
            }
            /*
            Check for success and assign the data to _pokemonDetail
             */
            is Resource.Success -> {
                result.data?.let { data ->
                    _pokemonDetail.value = data
                }
            }
            /*
            Check for loading state
             */
            is Resource.Loading -> {
                // Handle loading state
            }
        }
    }

    fun onSearchQueryChange(newValue: TextFieldValue) {
        searchQuery = newValue.copy(selection = TextRange(newValue.text.length))
        performSearch(newValue.text)
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch (Dispatchers.IO) {
            delay(300)
            val splitTokens = query.split(" ")

            val searchResult = _pokemonList.value.filter { pokemon ->
                splitTokens.all { string ->
                    // Check for name
                    pokemon.name.contains(string, ignoreCase = true)
                }
            }

            _searchResults.value = searchResult

        }
    }
}