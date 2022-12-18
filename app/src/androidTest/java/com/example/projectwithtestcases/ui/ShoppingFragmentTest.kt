package com.example.projectwithtestcases.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.projectwithtestcases.AndroidTestMainCoroutineRule
import com.example.projectwithtestcases.R
import com.example.projectwithtestcases.adapters.ShoppingItemAdapter
import com.example.projectwithtestcases.data.local.ShoppingItem
import com.example.projectwithtestcases.getOrAwaitValueAndroidTest
import com.example.projectwithtestcases.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    val mainCoroutineDispatcher = AndroidTestMainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory :ShoppingFragmentFactory

    @Inject
    lateinit var testFragmentFactory: TestShoppingFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }


    @Test
    fun swipeShoppingItem_deleteItemInDb(){
        val shoppingItem = ShoppingItem("Test",  1 ,1.0 , "Test" , 1)
        var testViewModel:ShoppingViewModel?=null
        launchFragmentInHiltContainer<ShoppingFragment>(fragmentFactory = testFragmentFactory) {
            testViewModel = viewModel
            viewModel?.insertShoppingItemIntoDb(shoppingItem)
        }
        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingItemViewHolder>(
                0,
                swipeLeft()
            )
        )
        testViewModel?.deleteShoppingItem(shoppingItem)
        assertThat(testViewModel?.shoppingItems?.getOrAwaitValueAndroidTest()).isEmpty()
    }
    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment(){
    val navController = mock(NavController::class.java)

    launchFragmentInHiltContainer<ShoppingFragment>(
        fragmentFactory = testFragmentFactory
    ) {
        Navigation.setViewNavController(requireView(), navController)
    }

    onView(withId(R.id.fabAddShoppingItem)).perform(click())

    verify(navController).navigate(
        ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
    )
    }

}