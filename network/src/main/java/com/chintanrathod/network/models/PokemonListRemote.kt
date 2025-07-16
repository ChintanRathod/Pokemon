package com.chintanrathod.network.models

import com.chintanrathod.domain.models.browse.PokemonListItem
import com.google.gson.annotations.SerializedName

/**
 * Data class that maps the Pokemon list response from the API.
 */
data class PokemonListRemote (
    @SerializedName("count")
    val count: Int,

    @SerializedName("next")
    val next: String?,

    @SerializedName("previous")
    val previous: String?,

    @SerializedName("results")
    val results: List<PokemonRemote>,
)

/**
 * Represents a single Pokemon item in the remote response.
 */
data class PokemonRemote(
    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String,
) {
    /**
     * Transforms the remote model into a domain model [PokemonListItem].
     */
    fun transform() = PokemonListItem (
        name = name,
        url = url
    )
}