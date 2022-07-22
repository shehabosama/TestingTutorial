package com.example.projectwithtestcases.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.projectwithtestcases.MainCoroutineRule
import com.example.projectwithtestcases.getOrAwaitValueTest
import com.example.projectwithtestcases.other.Constants
import com.example.projectwithtestcases.other.Status
import com.example.projectwithtestcases.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private lateinit var viewModel:ShoppingViewModel
    @Before
    fun setup(){
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `inset shopping item with empty field, returns error`(){
        viewModel.insertShoppingItem("name" , "" , "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `inset shopping item with too long name, returns error`(){
        val string = buildString {
            for(i in 1..Constants.MAX_NAME_LENGTH+1){
                append(1)
            }
        }
        viewModel.insertShoppingItem(string , "5" , "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `inset shopping item with too long price, returns error`(){
        val string = buildString {
            for(i in 1..Constants.MAX_PRICE_LENGTH+1){
                append(1)
            }
        }
        viewModel.insertShoppingItem("name" , "5" , string)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `inset shopping item with too high amount, returns error`(){

        viewModel.insertShoppingItem("name" , "999999999999999999999" , "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `inset shopping item with valid input, returns success`(){
        viewModel.insertShoppingItem("name", "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
    @Test
    fun `insert shopping item with valid input and empty string in the current image url function, return true`(){
        viewModel.insertShoppingItem("name", "5", "3.0")
        val valueOfShppingItemInsertion = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(valueOfShppingItemInsertion.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
        val valueOfCurrentShoppingItemImage = viewModel.curImageUrl.getOrAwaitValueTest()
        assertThat(valueOfCurrentShoppingItemImage).isEqualTo("")

    }
}