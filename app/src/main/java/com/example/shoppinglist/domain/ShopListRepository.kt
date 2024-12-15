package com.example.shoppinglist.domain

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun getShopItem(id: Int): ShopItem

    fun removeShopItem(shopItem: ShopItem)

    fun changeShopItem(shopItem: ShopItem)

    fun getShopList(): List<ShopItem>


}