package com.chintanrathod.domain.models.browse

/**
 * Represents the paginated response for a list of Pokémon.
 *
 * @property count Total number of available Pokemon.
 * @property next URL for the next page (could be null).
 * @property previous URL for the previous page (could be null).
 * @property results A list of individual Pokemon entries (could be null).
 */
data class PokemonListData(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ListItem>?,
)

/**
 * Represents a single Pokemon entry with basic details.
 *
 * @property name The name of the Pokemon.
 * @property url API URL to fetch detailed information about the Pokemon.
 */
data class ListItem (
    val name: String,
    val url: String
)