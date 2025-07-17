package com.chintanrathod.pokemon

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PokemonListDetailEndToEndTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /*
    In this instrumental test, we will launch the app then
    1. Check if screen list of pokemon
    2. Click on first pokemon
    3. Check for details present on the screen
    4. Go back
    5. Repeat 1-3 again with index 1
    6. Search for pokemon name bulbasaur
    7. Check for first item is present and perform click on it
     */
    @Test
    fun postAndCommentUITest() {

        /*
        Check for all pokemon item nodes
         */
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithTag("PokemonItem").fetchSemanticsNodes().isNotEmpty()
        }

        /*
        perform click on first item
         */
        composeTestRule.onAllNodesWithTag("PokemonItem")[0].performClick()

        /*
        it should load detail page
         */
        composeTestRule.onNodeWithTag("PokemonDetailName").assertExists()

        /*
        go back to list screen
         */
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        /*
        search for all pokemon item nodes
         */
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithTag("PokemonItem").fetchSemanticsNodes().isNotEmpty()
        }

        /*
        perform click on 2nd item
         */
        composeTestRule.onAllNodesWithTag("PokemonItem")[1].performClick()

        /*
        check if detail exist
         */
        composeTestRule.onNodeWithTag("PokemonDetailName").assertExists()

        /*
        go back to the list screen
         */
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        /*
        perform a search operation with pokemon name
         */
        composeTestRule.onNodeWithTag("PokemonSearch").performTextInput("bulbasaur")

        /*
        get all nodes of pokemon item
         */
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithTag("PokemonItem").fetchSemanticsNodes().isNotEmpty()
        }

        /*
        check if it exist
         */
        composeTestRule.onAllNodesWithTag("PokemonItem")[0].performClick()

    }
}