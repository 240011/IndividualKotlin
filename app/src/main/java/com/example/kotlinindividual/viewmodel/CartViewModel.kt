package com.example.kotlinindividual.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinindividual.model.CartItemModel
import com.example.kotlinindividual.model.ProductModel
import com.example.kotlinindividual.repository.CartRepository

class CartViewModel(private val cartRepo: CartRepository) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItemModel>>()
    val cartItems: LiveData<List<CartItemModel>> get() = _cartItems

    private val _cartTotal = MutableLiveData<Double>()
    val cartTotal: LiveData<Double> get() = _cartTotal

    private val _cartItemCount = MutableLiveData<Int>()
    val cartItemCount: LiveData<Int> get() = _cartItemCount

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun addToCart(product: ProductModel, quantity: Int = 1, callback: (Boolean, String) -> Unit) {
        val cartItem = CartItemModel(
            productId = product.productId,
            productName = product.productName,
            productPrice = product.productPrice,
            productDescription = product.productDescription,
            image = product.image,
            quantity = quantity
        )

        cartRepo.addToCart(cartItem, callback)
    }

    fun updateCartItemQuantity(cartId: String, newQuantity: Int, productPrice: Double, callback: (Boolean, String) -> Unit) {
        val updateData = mutableMapOf<String, Any?>(
            "quantity" to newQuantity,
            "totalPrice" to productPrice * newQuantity
        )
        cartRepo.updateCartItem(cartId, updateData, callback)
    }

    fun removeFromCart(cartId: String, callback: (Boolean, String) -> Unit) {
        cartRepo.removeFromCart(cartId, callback)
    }

    fun getCartItems() {
        _loading.postValue(true)
        cartRepo.getCartItems { items, success, message ->
            _loading.postValue(false)
            if (success) {
                _cartItems.postValue(items)
                updateCartSummary(items)
            } else {
                _cartItems.postValue(emptyList())
                _cartTotal.postValue(0.0)
                _cartItemCount.postValue(0)
            }
        }
    }

    fun clearCart(callback: (Boolean, String) -> Unit) {
        cartRepo.clearCart(callback)
    }

    private fun updateCartSummary(items: List<CartItemModel>) {
        val total = items.sumOf { it.totalPrice }
        val itemCount = items.sumOf { it.quantity }

        _cartTotal.postValue(total)
        _cartItemCount.postValue(itemCount)
    }
}