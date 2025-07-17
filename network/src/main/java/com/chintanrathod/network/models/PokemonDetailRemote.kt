package com.chintanrathod.network.models

import com.google.gson.annotations.SerializedName

/**
 * Data class that maps the Pokemon detail response from the API.
 */
data class PokemonDetailRemote (
    @SerializedName("base_experience")
    val baseExperience: Int,

    @SerializedName("height")
    val height: Int,

    @SerializedName("weight")
    val weight: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("sprites")
    val sprites: SpritesRemote,
)

/**
 * Represents a Sprites of a pokemon.
 */
data class SpritesRemote(
    @SerializedName("other")
    val other: SpritesOtherRemote,
)

/**
 * Represents other Sprites.
 */
data class SpritesOtherRemote(
    @SerializedName("official-artwork")
    val officialArtWork: OfficialArtWork,
)

/**
 * Represents a Official Art work image
 */
data class OfficialArtWork(
    @SerializedName("front_default")
    val frontDefault: String,
)