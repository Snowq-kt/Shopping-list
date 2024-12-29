package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.ChangeShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem

class ShopItemViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val changeShopItemUseCase = ChangeShopItemUseCase(repository)

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String>
        get() = _errorText

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    fun addShopItem(itemName: String?, itemCount: Int) {
        val name = parseItemName(itemName)
        val isCorrect = isDataCorrect(name, itemCount)
        if (isCorrect) {
            val item = ShopItem(name, itemCount, true)
            addShopItemUseCase.addShopItem(item)
            _shouldCloseScreen.value = Unit
        }
    }

    fun changeShopItem(itemName: String?, itemCount: Int) {
        val name = parseItemName(itemName)
        val isCorrect = isDataCorrect(name, itemCount)
        if (isCorrect) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = itemCount)
                changeShopItemUseCase.changeShopItem(item)
                _shouldCloseScreen.value = Unit
            }
        }
    }

    private fun parseItemName(itemName: String?): String {
        return itemName?.trim() ?: ""
    }

    private fun isDataCorrect(itemName: String, itemCount: Int): Boolean {
        var result = true
        if (itemName.isBlank()) {
            _errorText.value = "You cant create an item without text"
            result = false
        }
        if (itemCount <= 0 && itemCount.toString().isNotBlank()) {
            _errorText.value = "You cant create an item with count 0 or lower"
            result = false
        }
        return result
    }
}