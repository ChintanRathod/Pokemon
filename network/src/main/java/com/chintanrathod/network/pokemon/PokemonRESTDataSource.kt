package com.chintanrathod.network.pokemon

import com.chintanrathod.data.datasource.PokemonRemoteDataSource
import com.chintanrathod.domain.models.browse.PokemonListData
import com.chintanrathod.domain.models.detail.PokemonDetailData
import com.chintanrathod.network.common.retrofitApiCall
import com.chintanrathod.network.pokemon.api.PokemonService
import javax.inject.Inject

/**
 * Implementation of [PokemonRESTDataSource] that fetches list from a REST API using [PokemonService].
 *
 * Uses [retrofitApiCall] helper to make the network request and map the remote data
 * models to domain models ([PokemonListData]) by transforming each remote object.
 */
class PokemonRESTDataSource @Inject constructor(
    private val service: PokemonService
) : PokemonRemoteDataSource {

    /**
     * Fetches Pokémon list data from the network using Retrofit.
     *
     * @param url The API URL to fetch the data from.
     * @return A [NetworkResponse] containing either success or error.
     */
    override suspend fun getPokemonList(url: String) = retrofitApiCall(
        call = {
            service.getPokemonList(url = url)
        },
        parse = {
            PokemonListData(
                count = it.count,
                next = it.next,
                previous = it.previous,
                results = it.results.map { remote -> remote.transform() },
            )
        }
    )

    /**
     * Fetches detail of Pokémon from the network using Retrofit.
     *
     * @param id The id of the Pokemon.
     * @return A [NetworkResponse] containing either success or error.
     */
    override suspend fun getPokemonDetail(id: Int) = retrofitApiCall(
        call = {
            service.getPokemonDetail(id = id)
        },
        parse = {
            PokemonDetailData(
                height = it.height,
                weight = it.weight,
                baseExperience = it.baseExperience,
                image = it.sprites.other.officialArtWork.frontDefault,
                name = it.name
            )
        }
    )
}