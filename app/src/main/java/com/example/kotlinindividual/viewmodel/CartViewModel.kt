package com.example.kotlinindividual.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinindividual.model.CartItemModel
import com.example.kotlinindividual.repository.CartRepository

class CartViewModel(private val repo: CartRepository) : ViewModel() {

    // LiveData for a list of cart items
    private val _cartItems = MutableLiveData<List<CartItemModel>>()
    val cartItems: LiveData<List<CartItemModel>> get() = _cartItems

    // LiveData for loading state
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // Function to add an item to the cart
    fun addToCart(cartItem: CartItemModel) {
        _loading.postValue(true)
        repo.addToCart(cartItem) { success, msg ->
            _loading.postValue(false)
            // Optionally handle success or error messages
        }
    }

    // Function to update an item in the cart
    fun updateCartItem(cartItem: CartItemModel) {
        _loading.postValue(true)
        repo.updateCartItem(cartItem) { success, msg ->
            _loading.postValue(false)
            // Optionally handle success or error messages
        }
    }

    // Function to remove an item from the cart
    fun removeFromCart(itemId: String) {
        _loading.postValue(true)
        repo.removeFromCart(itemId) { success, msg ->
            _loading.postValue(false)
            // Optionally handle success or error messages
        }
    }

    // Function to clear the cart
    fun clearCart() {
        _loading.postValue(true)
        repo.clearCart() { success, msg ->
            _loading.postValue(false)
            // Optionally handle success or error messages
        }
    }

    // Function to get all cart items
    fun getCartItems() {
        _loading.postValue(true)
        repo.getCartItems { items, success, msg ->
            _loading.postValue(false)
            if (success) {
                _cartItems.postValue(items ?: emptyList())
            } else {
                _cartItems.postValue(emptyList())
            }
        }
    }

    // Function to get the count of cart items
    fun getCartItemCount() {
        repo.getCartItemCount { count, success, msg ->
            // Handle the count as needed
        }
    }
}
