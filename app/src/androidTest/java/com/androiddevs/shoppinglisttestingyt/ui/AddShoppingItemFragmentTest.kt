package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.launchFragmentInHiltContainer
import com.androiddevs.shoppinglisttestingyt.repositories.FakeShoppingRepositoryAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@MediumTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickInsertIntoDb_shoppingItemInsertedIntoDb(){
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            fragmentFactory = fragmentFactory
        ) {
            viewModel = testViewModel
        }
        onView(withId(R.id.etShoppingItemName)).perform(replaceText("shopping item"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("1"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("3.5"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testViewModel.shoppingItems.getOrAwaitValue())
            .contains(ShoppingItem("shopping item", 1, 3.5f, ""))
    }
}