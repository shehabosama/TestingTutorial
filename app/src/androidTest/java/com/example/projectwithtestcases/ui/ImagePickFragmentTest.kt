package com.example.projectwithtestcases.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.projectwithtestcases.R
import com.example.projectwithtestcases.adapters.ImageAdapter
import com.example.projectwithtestcases.data.local.ShoppingItem
import com.example.projectwithtestcases.getOrAwaitValueAndroidTest
import com.example.projectwithtestcases.launchFragmentInHiltContainer
import com.example.projectwithtestcases.other.Status
import com.example.projectwithtestcases.repositories.FakeShoppingRepositoryAndroidTest
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
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ImagePickFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory :ShoppingFragmentFactory
    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun clickImage_popBackStackAndSetImageUrl(){
        val navController = mock(NavController::class.java)
        val imageUrl = "TEST"
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<ImagePickFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.images= listOf(imageUrl)
            viewModel = testViewModel
        }
        onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                0,
               click()
            )
        )
        verify(navController).popBackStack()
        assertThat(testViewModel.curImageUrl.getOrAwaitValueAndroidTest()).isEqualTo(imageUrl)
    }
    @Test
    fun editTextListener_searchingForShoppingItem(){
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<ImagePickFragment>(fragmentFactory = fragmentFactory) {
            viewModel = testViewModel

        }
        onView(withId(R.id.etSearch)).perform(typeText("STRING_TO_BE_TYPED"), closeSoftKeyboard())
        val value = testViewModel.images.getOrAwaitValueAndroidTest()
        assertThat(value.getContentIfNotHandled()?.data).isEqualTo(Status.SUCCESS)

    }


}