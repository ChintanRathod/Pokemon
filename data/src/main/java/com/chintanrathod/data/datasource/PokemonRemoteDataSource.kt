package com.chintanrathod.data.datasource

import com.chintanrathod.data.common.NetworkResponse
import com.chintanrathod.domain.models.browse.PokemonListData

/**
 * Remote data source interface for fetching Pokemon-related data from the network.
 */
interface PokemonRemoteDataSource {

    /**
     * Fetches a list of Pokemon from the given API [url].
     *
     * Example URLs:
     * - https://pokeapi.co/api/v2/pokemon
     * - https://pokeapi.co/api/v2/pokemon?offset=20&limit=20
     *
     * @param url The API endpoint to fetch the Pokemon list.
     * @return A [NetworkResponse] wrapping [PokemonListData].
     */
    suspend fun getPokemonList(url: String): NetworkResponse<PokemonListData>
}