package com.chintanrathod.pokemon

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
     */
    @Test
    fun postAndCommentUITest() {

        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithTag("PokemonItem").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("PokemonItem")[0].performClick()

        composeTestRule.onNodeWithTag("PokemonDetailName").assertExists()

        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithTag("PokemonItem").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("PokemonItem")[1].performClick()

        composeTestRule.onNodeWithTag("PokemonDetailName").assertExists()
    }
}