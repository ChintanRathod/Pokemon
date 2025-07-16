package com.chintanrathod.data.usecase

import com.chintanrathod.data.usecases.GetPokemonListUseCaseImpl
import com.chintanrathod.domain.common.AppError
import com.chintanrathod.domain.common.Resource
import com.chintanrathod.domain.models.browse.PokemonListData
import com.chintanrathod.domain.models.browse.PokemonListItem
import com.chintanrathod.domain.repository.PokemonRepository
import com.chintanrathod.domain.usecase.GetPokemonListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

/*
This class will perform unit test on GetPokemonList use case
 */
class GetPokemonListUseCaseImplTest {

    private lateinit var repository: PokemonRepository
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @BeforeTest
    fun setUp() {
        repository = mockk()
        getPokemonListUseCase = GetPokemonListUseCaseImpl(repository)
    }

    /*
    Following test will fake the empty response of PokemonListData when
    repository.getPokemonList() is called from use case
     */
    @Test
    fun invoke_returnsSuccess_withEmptyPokemonList() = runBlocking {

        val initialUrl = "https://pokeapi.co/api/v2/pokemon"

        val fakeResponse = Resource.Success(
            PokemonListData(
                results = listOf(),
                count = 0,
                next = null,
                previous = null
            )
        )
        coEvery { repository.getPokemonList(initialUrl) } returns fakeResponse

        val result = getPokemonListUseCase(initialUrl)

        assertTrue(result is Resource.Success)
        assertEquals(fakeResponse, result)
        assertEquals(0, result.data?.results?.size)
        assertEquals(0, result.data?.count)
        assertEquals(null, result.data?.next)
        assertEquals(null, result.data?.previous)
    }

    /*
    Following test will return an list of pokemon with success
     */
    @Test
    fun invoke_returnsSuccess_withNonEmptyPokemonList() = runBlocking {

        val initialUrl = "https://pokeapi.co/api/v2/pokemon"

        val pokemonList = listOf(
            PokemonListItem(name = "Pokemon 1", url = "url"),
            PokemonListItem(name = "Pokemon 2", url = "url"),
        )
        val fakeResponse = Resource.Success(
            PokemonListData(
                count = 2,
                next = "next",
                previous = null,
                results = pokemonList
            )
        )
        coEvery { repository.getPokemonList(initialUrl) } returns fakeResponse

        val result = getPokemonListUseCase(initialUrl)

        assertTrue(result is Resource.Success)
        assertEquals(pokemonList, result.data?.results)
        assertEquals(2, result.data?.count)
        assertEquals("next", result.data?.next)
        assertEquals(null, result.data?.previous)

    }

    /*
    Following test will create an No internet error with error response
     */
    @Test
    fun invoke_returnsError_whenRepositoryFails() = runBlocking {

        val initialUrl = "https://pokeapi.co/api/v2/pokemon"

        val error = Resource.Error<PokemonListData>(AppError.NoInternet)
        coEvery { repository.getPokemonList(initialUrl) } returns error

        val result = getPokemonListUseCase(initialUrl)

        assertTrue(result is Resource.Error)
        assertEquals(AppError.NoInternet, result.error)
    }

    @Test
    fun invoke_returnsLoading_whenRepositoryReturnsLoading() = runBlocking {

        val initialUrl = "https://pokeapi.co/api/v2/pokemon"

        val loading = Resource.Loading<PokemonListData>()
        coEvery { repository.getPokemonList(initialUrl) } returns loading

        val result = getPokemonListUseCase(initialUrl)

        assertTrue(result is Resource.Loading)
    }
}