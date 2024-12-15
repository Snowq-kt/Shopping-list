package com.example.shoppinglist.domain

data class ShopItem(
    val name: String,
    val count: Int,
    val id: Int,
    val isActive: Boolean
)
