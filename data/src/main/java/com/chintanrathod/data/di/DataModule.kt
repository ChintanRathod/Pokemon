package com.chintanrathod.data.di

import com.chintanrathod.data.datasource.PokemonRemoteDataSource
import com.chintanrathod.data.repository.PokemonDataRepository
import com.chintanrathod.data.usecases.GetPokemonDetailUseCaseImpl
import com.chintanrathod.data.usecases.GetPokemonListUseCaseImpl
import com.chintanrathod.domain.repository.PokemonRepository
import com.chintanrathod.domain.usecase.GetPokemonDetailUseCase
import com.chintanrathod.domain.usecase.GetPokemonListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides dependencies related to data layer,
 * including repository and use case bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
     * Provides an implementation of [PokemonRepository] that uses the remote data source.
     *
     * @param network The remote data source used to fetch Pokémon data.
     * @return An instance of [PokemonRepository].
     */
    @Provides
    @Singleton
    fun providePokemonRepository(
        network: PokemonRemoteDataSource
    ): PokemonRepository {
        return PokemonDataRepository(network)
    }

    /**
     * Provides an implementation of [GetPokemonListUseCase] that uses the repository.
     *
     * @param pokemonRepository The repository used to get Pokémon list data.
     * @return An instance of [GetPokemonListUseCase].
     */
    @Provides
    @Singleton
    fun provideGetAllPokemonUseCase(
        pokemonRepository: PokemonRepository
    ): GetPokemonListUseCase {
        return GetPokemonListUseCaseImpl(pokemonRepository)
    }

    /**
     * Provides an implementation of [GetPokemonListUseCase] that uses the repository.
     *
     * @param pokemonRepository The repository used to get Pokémon list data.
     * @return An instance of [GetPokemonListUseCase].
     */
    @Provides
    @Singleton
    fun provideGetPokemonDetailUseCase(
        pokemonRepository: PokemonRepository
    ): GetPokemonDetailUseCase {
        return GetPokemonDetailUseCaseImpl(pokemonRepository)
    }
}