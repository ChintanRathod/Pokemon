package com.chintanrathod.network.pokemon.api

import com.chintanrathod.network.models.PokemonDetailRemote
import com.chintanrathod.network.models.PokemonListRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * Retrofit API interface for fetching pokemon data.
 */
interface PokemonService {

    /**
     * Fetches a paginated list of Pokémon from the given [url].
     *
     * Examples:
     * - https://pokeapi.co/api/v2/pokemon
     * - https://pokeapi.co/api/v2/pokemon?offset=20&limit=20
     *
     * @param url The full API endpoint URL to fetch data.
     * @return A [Response] wrapping [PokemonListRemote].
     */
    @GET
    suspend fun getPokemonList(@Url url: String): Response<PokemonListRemote>

    /**
     * Fetches a detail of Pokemon for the given [id].
     *
     * Examples:
     * - https://pokeapi.co/api/v2/pokemon/1
     *
     * @param id The id of pokemon.
     * @return A [Response] wrapping [PokemonDetailRemote].
     */
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): Response<PokemonDetailRemote>
}