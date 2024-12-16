package com.example.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.ChangeShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.GetShopListUseCase
import com.example.shoppinglist.domain.RemoveShopItemUseCase
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

class MainViewModel : ViewModel() {

    private val shopListRepository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(shopListRepository)
    private val removeShopItemUseCase = RemoveShopItemUseCase(shopListRepository)
    private val changeShopItemUseCase = ChangeShopItemUseCase(shopListRepository)

    val shopList = MutableLiveData<List<ShopItem>>()

    fun getShopList() {
        shopList.value = getShopListUseCase.getShopList()
    }

    fun removeShopItem(shopItem: ShopItem) {
        removeShopItemUseCase.removeShopItem(shopItem)
        getShopList()
    }

    fun changeShopItemState(shopItem: ShopItem) {
        val newShopItem = shopItem.copy(isActive = !shopItem.isActive)
        changeShopItemUseCase.changeShopItem(newShopItem)
        getShopList()
    }

}