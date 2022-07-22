package com.example.projectwithtestcases.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.projectwithtestcases.getOrAwaitValueAndroidTest
import com.example.projectwithtestcases.ui.ShoppingFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

/** we will replace AndroidJUnit4 by our own custom AndroidJUnit4 with HiltTestRunner
 ****************** @RunWith(AndroidJUnit4::class)******************
 **/
@HiltAndroidTest
@SmallTest // or mid or large
@ExperimentalCoroutinesApi
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase

    private lateinit var dao:ShoppingDao
    @Before
    fun setup(){
        hiltRule.inject()
        dao = database.shoppingDao()
    }
    @After
    fun tearDown(){
       database.close()
    }



    @Test
    fun insetShoppingItem()= runBlockingTest {
        val shoppingItem = ShoppingItem("name" , 1 , 1.0 , "url" , id  =1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValueAndroidTest()

        assertThat(allShoppingItem.contains(shoppingItem))
    }
    @Test
    fun deleteShoppingItem()= runBlocking {
        val shoppingItem = ShoppingItem("name" , 1 , 1.0 , "url" , id  =1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)
        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValueAndroidTest()
        assertThat(allShoppingItem).doesNotContain(shoppingItem)
    }

    @Test
    fun oberveTotalPriceSum()= runBlocking {
        val shoppingItem1 = ShoppingItem("name" , 2 , 10.0 , "url" , id  =1)
        val shoppingItem2 = ShoppingItem("name" , 4 , 5.5 , "url" , id  =2)
        val shoppingItem3 = ShoppingItem("name" , 0 , 100.0 , "url" , id  =3)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValueAndroidTest()

        assertThat(totalPriceSum).isEqualTo(2*10.0 + 4*5.5 )

    }

}