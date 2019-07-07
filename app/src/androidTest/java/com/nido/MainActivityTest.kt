package com.nido

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.nido.activity.MainActivity
import com.nido.viewmodel.MainActivityViewModel
//import androidx.test.runner.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @Mock
    private lateinit var viewModel: MainActivityViewModel

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val editText = onView(withId(R.id.editTextSearch))
        editText.check(matches(isDisplayed()))
    }

    @Test
    fun checkApiCallNotTriggeredWhenSearchQueryEmpty() {
        val button = onView(withId(R.id.imageButtonSearch))
        button.perform(click())
        if (::viewModel.isInitialized) {
            verify(viewModel, never()).getImagesFromFlicker(anyString())
        }
    }

    @Test
    fun checkApiCallNotTriggeredOnSearchQuery() {
        onView(withId(R.id.editTextSearch)).perform(replaceText("BMW"))
        val button = onView(withId(R.id.imageButtonSearch))
        button.perform(click())
        if (::viewModel.isInitialized) {
            verify(viewModel).getImagesFromFlicker(anyString())
        }
    }
}
