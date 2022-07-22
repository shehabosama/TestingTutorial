package com.example.projectwithtestcases.repositories

import androidx.lifecycle.LiveData
import com.example.projectwithtestcases.data.local.ShoppingItem
import com.example.projectwithtestcases.data.remote.responses.ImageResponse
import com.example.projectwithtestcases.other.Resource

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Double>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}