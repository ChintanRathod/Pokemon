package com.chintanrathod.domain.usecase

import com.chintanrathod.domain.common.Resource
import com.chintanrathod.domain.models.browse.PokemonListData

/**
 * Use case interface for fetching a list of Pokémon.
 *
 * This contract defines a suspendable function that, when invoked,
 * retrieves Pokemon list data wrapped in a [Resource] to represent loading, success, or error states.
 *
 * @param url The API endpoint to fetch the Pokemon list.
 * @return [Resource] containing [PokemonListData] on success, or error state otherwise.
 */
interface GetPokemonListUseCase {
    suspend operator fun invoke(url: String): Resource<PokemonListData>
}