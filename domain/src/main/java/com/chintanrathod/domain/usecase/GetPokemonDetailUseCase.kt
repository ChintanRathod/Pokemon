package com.chintanrathod.domain.usecase

import com.chintanrathod.domain.common.Resource
import com.chintanrathod.domain.models.browse.PokemonListData
import com.chintanrathod.domain.models.detail.PokemonDetailData

/**
 * Use case interface for fetching a detail of Pokemon.
 *
 * This contract defines a suspendable function that, when invoked,
 * retrieves Pokemon detail data wrapped in a [Resource] to represent loading, success, or error states.
 *
 * @param id The API endpoint to fetch the Pokemon detail.
 * @return [Resource] containing [PokemonDetailData] on success, or error state otherwise.
 */
interface GetPokemonDetailUseCase {
    suspend operator fun invoke(id: Int): Resource<PokemonDetailData>
}