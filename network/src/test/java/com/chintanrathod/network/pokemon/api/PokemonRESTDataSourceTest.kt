package com.chintanrathod.network.pokemon.api

import com.chintanrathod.data.common.NetworkResponse
import com.chintanrathod.network.pokemon.PokemonRESTDataSource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.Test

/**
 * This class will test possible success and error state of Pokemon Data source by mocking
 * service with MockWebServer
 */
class PokemonRESTDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var pokemonService: PokemonService
    private lateinit var dataSource: PokemonRESTDataSource

    private val initialUrl = "/api/v2/pokemon"

    @Before
    fun setup() {
        System.setProperty("okhttp.platform", "org.conscrypt.OpenSSLProvider")

        mockWebServer = MockWebServer()
        mockWebServer.start()

        pokemonService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonService::class.java)

        dataSource = PokemonRESTDataSource(pokemonService)
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }

    /*
    Check for 200 success parsing
     */
    @Test
    fun getPokemonList_returns_Success_200() = runBlocking {

        val mockJson = """
            {
                "count": 1302,
                "next": "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
                "previous": null,
                "results": [
                    {
                        "name": "bulbasaur",
                        "url": "https://pokeapi.co/api/v2/pokemon/1/"
                    },
                    {
                        "name": "ivysaur",
                        "url": "https://pokeapi.co/api/v2/pokemon/2/"
                    }
                ]
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockJson)
        )

        val result = dataSource.getPokemonList(initialUrl)

        assertTrue(result is NetworkResponse.Success)
        val pokemonList = (result as NetworkResponse.Success).response.results
        assertEquals(2, pokemonList?.size)
        assertEquals("bulbasaur", pokemonList?.get(0)?.name)
    }

    /*
    Check for bad request
     */
    @Test
    fun getPokemonList_returns_ClientError_400() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("Bad Request")
        )

        val result = dataSource.getPokemonList(initialUrl)

        assertTrue(result is NetworkResponse.ClientError)
        assertEquals(400, (result as NetworkResponse.ClientError).code)
    }

    /*
    Check for server error 500
     */
    @Test
    fun getPokemonList_returns_ClientError_500() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Bad Request")
        )

        val result = dataSource.getPokemonList(initialUrl)

        assertTrue(result is NetworkResponse.ServerError)
        assertEquals(500, (result as NetworkResponse.ServerError).code)
    }

    /*
    Check for No internet
     */
    @Test
    fun getPokemonList_returns_NoInternet() = runBlocking {
        mockWebServer.shutdown()

        val result = dataSource.getPokemonList(initialUrl)

        assertTrue(result is NetworkResponse.NoInternet)
    }

    /*
    Following test we will try to break by passing wrong json
     */
    @Test
    fun getPokemonList_returns_Success_withParsingError() = runBlocking {
        // To mock parsing error, removed array []
        val mockJson = """
             [
                {
                    "name": "bulbasaur",
                    "url": "https://pokeapi.co/api/v2/pokemon/1/"
                },
                {
                    "name": "ivysaur",
                    "url": "https://pokeapi.co/api/v2/pokemon/2/"
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockJson)
        )

        val result = dataSource.getPokemonList(initialUrl)

        assertTrue(result is NetworkResponse.ClientError)
        assertEquals(-1, (result as NetworkResponse.ClientError).code)
    }

    //

    /*
    Check for 200 success parsing
     */
    @Test
    fun getPokemonDetail_returns_Success_200() = runBlocking {

        val mockJson = """
            {
                "base_experience": 64,
                "height": 7,
                "weight": 7,
                "name": "bulbasaur",
                "sprites": {
                    "other": {
                        "official-artwork": {
                            "front_default": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
                        }
                    }
                }
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockJson)
        )

        val result = dataSource.getPokemonDetail(id = 1)

        assertTrue(result is NetworkResponse.Success)
        val pokemonDetail = (result as NetworkResponse.Success).response
        assertEquals(7, pokemonDetail.height)
        assertEquals("bulbasaur", pokemonDetail.name)
        assertEquals("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png", pokemonDetail.image)
    }

    /*
    Check for bad request
     */
    @Test
    fun getPokemonDetail_returns_ClientError_400() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("Bad Request")
        )

        val result = dataSource.getPokemonDetail(id = 1)

        assertTrue(result is NetworkResponse.ClientError)
        assertEquals(400, (result as NetworkResponse.ClientError).code)
    }

    /*
    Check for server error 500
     */
    @Test
    fun getPokemonDetail_returns_ClientError_500() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Bad Request")
        )

        val result = dataSource.getPokemonDetail(id = 1)

        assertTrue(result is NetworkResponse.ServerError)
        assertEquals(500, (result as NetworkResponse.ServerError).code)
    }

    /*
    Check for No internet
     */
    @Test
    fun getPokemonDetail_returns_NoInternet() = runBlocking {
        mockWebServer.shutdown()

        val result = dataSource.getPokemonDetail(id = 1)

        assertTrue(result is NetworkResponse.NoInternet)
    }

    /*
    Following test we will try to break by passing wrong json
     */
    @Test
    fun getPokemonDetail_returns_Success_withParsingError() = runBlocking {
        // To mock parsing error, removed array []
        val mockJson = """
             {
                "base_experience": 64,
                "height": 7,
                "weight": 7,
                "name": "bulbasaur",
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockJson)
        )

        val result = dataSource.getPokemonDetail(id = 1)

        assertTrue(result is NetworkResponse.ClientError)
        assertEquals(-1, (result as NetworkResponse.ClientError).code)
    }
}