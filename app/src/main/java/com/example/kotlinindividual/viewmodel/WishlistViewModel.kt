package com.example.kotlinindividual.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinindividual.model.WishlistItemModel
import com.example.kotlinindividual.repository.WishlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WishlistViewModel(private val repository: WishlistRepository) : ViewModel() {

    private val _wishlistItems = MutableStateFlow<List<WishlistItemModel>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistItemModel>> = _wishlistItems

    init {
        viewModelScope.launch {
            repository.getWishlistItems().collect {
                _wishlistItems.value = it
            }
        }
    }

    fun addToWishlist(item: WishlistItemModel) {
        viewModelScope.launch {
            repository.addToWishlist(item)
        }
    }

    fun removeFromWishlist(item: WishlistItemModel) {
        viewModelScope.launch {
            repository.removeFromWishlist(item)
        }
    }
}