package com.chintanrathod.domain.repository

import com.chintanrathod.domain.common.Resource
import com.chintanrathod.domain.models.browse.PokemonListData

/**
 * Repository interface for accessing Pokemon-related data.
 *
 * Defines a contract for retrieving a paginated list of Pokemon from a given URL.
 * This allows clients to fetch the initial list as well as subsequent pages dynamically.
 */
interface PokemonRepository {

    /**
     * This method will allow user to get first n (20) pokemon list
     * the purpose of URL is to call dynamic URL for next time when
     * user reaches to the end of the list
     *
     * Ex.
     * - https://pokeapi.co/api/v2/pokemon
     * - https://pokeapi.co/api/v2/pokemon?offset=20&limit=20
     * - https://pokeapi.co/api/v2/pokemon?offset=40&limit=20
     *
     * @param url The full URL to fetch the Pokémon list from.
     * @return [Resource] containing [PokemonListData] on success, or an error state otherwise.
     */
    suspend fun getPokemonList(url: String): Resource<PokemonListData>
}