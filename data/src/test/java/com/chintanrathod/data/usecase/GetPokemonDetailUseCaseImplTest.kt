package com.chintanrathod.data.usecase

import com.chintanrathod.data.usecases.GetPokemonDetailUseCaseImpl
import com.chintanrathod.domain.common.AppError
import com.chintanrathod.domain.common.Resource
import com.chintanrathod.domain.models.detail.PokemonDetailData
import com.chintanrathod.domain.repository.PokemonRepository
import com.chintanrathod.domain.usecase.GetPokemonDetailUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

/*
This class will perform unit test on GetPokemonDetail use case
 */
class GetPokemonDetailUseCaseImplTest {

    private lateinit var repository: PokemonRepository
    private lateinit var getPokemonDetailUseCase: GetPokemonDetailUseCase

    @BeforeTest
    fun setUp() {
        repository = mockk()
        getPokemonDetailUseCase = GetPokemonDetailUseCaseImpl(repository)
    }

    /*
    Following test will fake the response of PokemonDetailData when
    repository.getPokemonDetail() is called from use case
     */
    @Test
    fun invoke_returnsSuccess_withPokemonDetail() = runBlocking {

        val fakeResponse = Resource.Success(
            PokemonDetailData(
                height = 1,
                weight = 1,
                baseExperience = 1,
                image = "Url",
                name = "name"
            )
        )
        coEvery { repository.getPokemonDetail(id = 1) } returns fakeResponse

        val result = getPokemonDetailUseCase(id = 1)

        assertTrue(result is Resource.Success)
        assertEquals(fakeResponse, result)
    }

    /*
    Following test will create an No internet error with error response
     */
    @Test
    fun invoke_returnsError_whenRepositoryFails() = runBlocking {

        val error = Resource.Error<PokemonDetailData>(AppError.NoInternet)
        coEvery { repository.getPokemonDetail(id = 1) } returns error

        val result = getPokemonDetailUseCase(id = 1)

        assertTrue(result is Resource.Error)
        assertEquals(AppError.NoInternet, result.error)
    }

    @Test
    fun invoke_returnsLoading_whenRepositoryReturnsLoading() = runBlocking {

        val loading = Resource.Loading<PokemonDetailData>()
        coEvery { repository.getPokemonDetail(id = 1) } returns loading

        val result = getPokemonDetailUseCase(id = 1)

        assertTrue(result is Resource.Loading)
    }
}