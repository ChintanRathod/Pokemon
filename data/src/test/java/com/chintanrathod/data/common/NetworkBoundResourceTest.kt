package com.chintanrathod.data.common

import com.chintanrathod.domain.common.AppError
import com.chintanrathod.domain.common.Resource
import com.chintanrathod.domain.models.browse.PokemonListData
import com.chintanrathod.domain.models.browse.PokemonListItem
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkBoundResourceTest {

    /*
    Test for the empty success from network
     */
    @Test
    fun networkBoundResource_returnsResourceSuccess_whenResponseIsEmptyList() = runBlocking {
        val result = networkBoundResource {
            NetworkResponse.Success(
                PokemonListData(
                    results = listOf(),
                    count = 10,
                    next = "next",
                    previous = "previous"
                )
            )
        }

        assertTrue(result is Resource.Success)
    }

    /*
    Test for the expected list in success from network
     */
    @Test
    fun networkBoundResource_returnsResourceSuccess_withCorrectData() = runBlocking {
        val expectedPokemonList = listOf(
            PokemonListItem(name = "Pokemon 1", url = "url"),
            PokemonListItem(name = "Pokemon 2", url = "url"),
        )
        val expectedData = PokemonListData(
            count = 10,
            next = "next",
            previous = "previous",
            results = expectedPokemonList
        )

        val result = networkBoundResource {
            NetworkResponse.Success(expectedData)
        }

        assertTrue(result is Resource.Success)
        assertEquals(expectedData, result.data)
    }

    /*
    Test for Client error from network
     */
    @Test
    fun networkBoundResource_returnsResourceError_withClientError() = runBlocking {
        val result = networkBoundResource<PokemonListData> {
            NetworkResponse.ClientError(code = 400, message = "Bad request")
        }

        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).error is AppError.NetworkError)
    }

    /*
    Test for server error from network
     */
    @Test
    fun networkBoundResource_returnsResourceError_withServerError() = runBlocking {
        val result = networkBoundResource<PokemonListData> {
            NetworkResponse.ServerError(code = 500, message = "Server Error")
        }

        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).error is AppError.NetworkError)
    }

    /*
    Test for no internet
     */
    @Test
    fun networkBoundResource_returnsResourceError_withNoInternet() = runBlocking {
        val result = networkBoundResource<PokemonListData> {
            NetworkResponse.NoInternet()
        }

        assertTrue(result is Resource.Error)
        assertEquals(AppError.NoInternet, (result as Resource.Error).error)
    }
}