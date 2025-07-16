package com.chintanrathod.data.datasource

import com.chintanrathod.data.common.NetworkResponse
import com.chintanrathod.domain.models.browse.PokemonListData
import com.chintanrathod.domain.models.detail.PokemonDetailData

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

    /**
     * Fetches a detail of Pokemon from the given API [url].
     *
     * Example URLs:
     * - https://pokeapi.co/api/v2/pokemon/1
     *
     * @param url The API endpoint to fetch the Pokemon list.
     * @return A [NetworkResponse] wrapping [PokemonDetailData].
     */
    suspend fun getPokemonDetail(id: Int): NetworkResponse<PokemonDetailData>
}