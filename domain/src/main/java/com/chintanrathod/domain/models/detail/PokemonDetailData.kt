package com.chintanrathod.domain.models.detail

/**
 * Represents the paginated response for a list of Pokémon.
 *
 * @property height Height of Pokemon.
 * @property weight Weight of Pokemon.
 * @property baseExperience Experience of Pokemon.
 * @property image URL of image of Pokemon.
 * @property name Name of Pokemon.
 */
data class PokemonDetailData(
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val image: String?,
    val name: String?,
)