package com.chintanrathod.data.repository

import com.chintanrathod.data.common.networkBoundResource
import com.chintanrathod.data.datasource.PokemonRemoteDataSource
import com.chintanrathod.domain.repository.PokemonRepository
import javax.inject.Inject

/**
 * Implementation of [PokemonRepository] that fetches data from the network.
 *
 * @param network The remote data source that provides Pokémon data.
 */
internal class PokemonDataRepository @Inject constructor(
    private val network: PokemonRemoteDataSource,
) : PokemonRepository {

    /**
     * Fetches a list of Pokémon from the remote source using the given [url].
     *
     * @param url The API endpoint to fetch data from.
     * @return A [Resource] wrapping the result or error.
     */
    override suspend fun getPokemonList(url: String) = networkBoundResource(
        network = { network.getPokemonList(url)},
    )

    /**
     * Fetches a detail of Pokemon from the remote source using the given [id].
     *
     * @param id Id of the Pokemon.
     * @return A [Resource] wrapping the result or error.
     */
    override suspend fun getPokemonDetail(id: Int) = networkBoundResource(
        network = { network.getPokemonDetail(id = id)},
    )
}