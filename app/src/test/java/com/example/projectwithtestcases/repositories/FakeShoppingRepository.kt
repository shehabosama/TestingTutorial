package com.example.projectwithtestcases.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projectwithtestcases.data.local.ShoppingItem
import com.example.projectwithtestcases.data.remote.responses.ImageResponse
import com.example.projectwithtestcases.other.Resource

 class FakeShoppingRepository:ShoppingRepository {

    private val shoppingItems = mutableListOf<ShoppingItem>()
    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Double>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value:Boolean){
        this.shouldReturnNetworkError = value
    }

    private fun refreshLiveData(){
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }
    private fun getTotalPrice():Double{
        return shoppingItems.sumByDouble { it.price }
    }
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
       shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
      shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
       return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Double> {
        return observableTotalPrice
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if(shouldReturnNetworkError){
            Resource.error("Error" , null)
        }else{
            Resource.success(ImageResponse(listOf(),0,0))
        }
    }
}