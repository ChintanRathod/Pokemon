package com.chintanrathod.data.usecases

import com.chintanrathod.domain.common.Resource
import com.chintanrathod.domain.models.browse.PokemonListData
import com.chintanrathod.domain.repository.PokemonRepository
import com.chintanrathod.domain.usecase.GetPokemonListUseCase
import javax.inject.Inject

/**
 * Use case implementation for fetching Pokémon list.
 *
 * @param pokemonRepository The repository to fetch data from.
 */
class GetPokemonListUseCaseImpl @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : GetPokemonListUseCase {

    /**
     * Invokes the use case to fetch a Pokémon list from the given [url].
     *
     * @param url The API endpoint to fetch the Pokémon list.
     * @return A [Resource] containing the Pokémon list or an error.
     */
    override suspend fun invoke(url: String): Resource<PokemonListData> {
        return pokemonRepository.getPokemonList(url)
    }
}