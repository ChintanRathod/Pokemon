package com.chintanrathod.pokemon

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chintanrathod.domain.common.AppError
import com.chintanrathod.domain.common.Resource
import com.chintanrathod.domain.models.browse.PokemonListData
import com.chintanrathod.domain.models.browse.PokemonListItem
import com.chintanrathod.domain.usecase.GetPokemonListUseCase
import com.chintanrathod.pokemon.viewmodel.PokemonViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Test dispatcher for coroutines, allows controlling coroutine execution.
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    private lateinit var viewModel: PokemonViewModel

    private var initialUrl = "https://pokeapi.co/api/v2/pokemon"

    @Before
    fun setup() {
        // Set the main dispatcher to our test dispatcher for coroutine testing
        Dispatchers.setMain(testDispatcher)

        // Initialize the mock for GetPokemonListUseCase
        // Using MockK:
        getPokemonListUseCase = mockk(relaxed = true)
        // Using Mockito-Kotlin:
        // GetPokemonListUseCase = mock()

        // Initialize the ViewModel with the mocked dependency
        viewModel = PokemonViewModel(getPokemonListUseCase, testDispatcher)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after tests
        Dispatchers.resetMain()
    }

    @Test
    fun loadPokemonList_emits_list_when_use_case_returns_success() = runTest {
        val dummyList = listOf(
            PokemonListItem(name = "Name 1", url = "Url 1"),
            PokemonListItem(name = "Name 2", url = "Url 2")
        )
        val pokemonListData = PokemonListData(
            count = 2,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = "",
            results = dummyList
        )

        coEvery { getPokemonListUseCase(initialUrl) } returns Resource.Success(pokemonListData)

        viewModel.loadPokemonList(initialUrl)

        advanceUntilIdle()

        val pokemonList = viewModel.pokemonList.value

        assertEquals(dummyList, pokemonList)
        assertEquals(2, pokemonList.size)
    }

    @Test
    fun loadPokemonList_does_not_emit_list_when_use_case_returns_error() = runTest {
        coEvery { getPokemonListUseCase(initialUrl) } returns Resource.Error(error = AppError.NetworkError(
            code = 1,
            message = "Something went wrong")
        )

        viewModel.loadPokemonList(initialUrl)

        advanceUntilIdle()

        assertTrue(viewModel.pokemonList.value.isEmpty())
    }

    @Test
    fun loadPokemonList_emits_empty_list_when_use_case_returns_empty_data() = runTest {
        val fakeResponse = Resource.Success(
            PokemonListData(
                count = 0,
                next = null,
                previous = null,
                results = emptyList()
            )
        )
        coEvery { getPokemonListUseCase(initialUrl) } returns fakeResponse

        viewModel.loadPokemonList(initialUrl)

        advanceUntilIdle()

        assertTrue(viewModel.pokemonList.value.isEmpty())
    }
}