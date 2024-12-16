package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun getShopItem(id: Int): ShopItem

    fun removeShopItem(shopItem: ShopItem)

    fun changeShopItem(shopItem: ShopItem)

    fun getShopList(): LiveData<List<ShopItem>>


}