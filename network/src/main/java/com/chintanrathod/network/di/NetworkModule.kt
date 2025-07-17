package com.chintanrathod.network.di

import com.chintanrathod.data.datasource.PokemonRemoteDataSource
import com.chintanrathod.network.pokemon.PokemonRESTDataSource
import com.chintanrathod.network.pokemon.api.PokemonService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt module that provides network-related dependencies like Retrofit, OkHttpClient,
 * API service, and remote data source.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of [OkHttpClient].
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    /**
     * Provides a singleton instance of [Retrofit] configured with the base URL
     * and Gson converter.
     *
     * @param client The [OkHttpClient] to use for HTTP requests.
     */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * Provides a singleton implementation of [PokemonRemoteDataSource] using the REST API.
     *
     * @param pokemonService The [PokemonService] instance to interact with the network.
     */
    @Provides
    @Singleton
    fun providePokemonRemoteDataSource(
        pokemonService: PokemonService
    ): PokemonRemoteDataSource {
        return PokemonRESTDataSource(pokemonService)
    }

    /**
     * Provides a singleton instance of [PokemonService] created by Retrofit.
     *
     * @param retrofit The [Retrofit] instance to create the service.
     */
    @Provides
    @Singleton
    fun providePokemonService(retrofit: Retrofit): PokemonService {
        return retrofit.create(PokemonService::class.java)
    }
}