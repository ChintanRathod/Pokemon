package com.chintanrathod.data.usecases

import com.chintanrathod.domain.common.Resource
import com.chintanrathod.domain.models.browse.PokemonListData
import com.chintanrathod.domain.models.detail.PokemonDetailData
import com.chintanrathod.domain.repository.PokemonRepository
import com.chintanrathod.domain.usecase.GetPokemonDetailUseCase
import com.chintanrathod.domain.usecase.GetPokemonListUseCase
import javax.inject.Inject

/**
 * Use case implementation for fetching Pokemon details.
 *
 * @param pokemonRepository The repository to fetch data from.
 */
class GetPokemonDetailUseCaseImpl @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : GetPokemonDetailUseCase {

    /**
     * Invokes the use case to fetch a Pokemon detail for the given [id].
     *
     * @param id Id of Pokemon to fetch the details of.
     * @return A [Resource] containing the Pokémon detail or an error.
     */
    override suspend fun invoke(id: Int): Resource<PokemonDetailData> {
        return pokemonRepository.getPokemonDetail(id = id)
    }
}