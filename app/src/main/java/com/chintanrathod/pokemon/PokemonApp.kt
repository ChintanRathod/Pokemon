package com.chintanrathod.pokemon

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main application class for the Pokémon app.
 * Annotated with [@HiltAndroidApp] to trigger Hilt's code generation.
 */
@HiltAndroidApp
class PokemonApp : Application()