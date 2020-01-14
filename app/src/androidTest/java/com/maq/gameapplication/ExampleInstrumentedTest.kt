package com.maq.gameapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.maq.gameapplication.ui.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)


    @Test fun findView(){

        onView(withId(R.id.score))
        onView(withId(R.id.section))
        onView(withId(R.id.button_read)).check(matches(withText("Read Article")))
        onView(withId(R.id.button_skip)).check(matches(withText("Skip")))
        onView(withId(R.id.button_headline01))
        onView(withId(R.id.button_headline02))
        onView(withId(R.id.button_headline03))
        onView(withId(R.id.button_nextquestion)).check(matches(withText("Next Question")))
    }

    @Test fun buttonClick() {
        // withId(R.id.my_view) is a ViewMatcher
        // click() is a ViewAction
        // matches(isDisplayed()) is a ViewAssertion
        onView(withId(R.id.button_read))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(withId(R.id.button_skip))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(withId(R.id.button_headline01))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(withId(R.id.button_headline02))
            .perform(click())
            .check(matches(isDisplayed()))

        onView(withId(R.id.button_headline03))
            .perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.maq.gameapplication", appContext.packageName)
    }
}
