package com.example.kotlinindividual.repository

import com.example.kotlinindividual.model.CartItemModel

interface CartRepository {
        fun addToCart(cartItem: CartItemModel, callback: (Boolean, String) -> Unit) {
            // Implementation for adding to cart
        }

        fun updateCartItem(cartItem: CartItemModel, callback: (Boolean, String) -> Unit) {
            // Implementation for updating cart item
        }

        fun removeFromCart(itemId: String, callback: (Boolean, String) -> Unit) {
            // Implementation for removing from cart
        }

        fun clearCart(callback: (Boolean, String) -> Unit) {
            // Implementation for clearing the cart
        }

        fun getCartItems(callback: (List<CartItemModel>?, Boolean, String) -> Unit) {
            // Implementation for getting cart items
        }

        fun getCartItemCount(callback: (Int, Boolean, String) -> Unit) {
            // Implementation for getting cart item count
        }

        suspend fun addToCart(cartItem: CartItemModel) {
            // Coroutine implementation for adding to cart
        }

        suspend fun updateCartItem(cartItem: CartItemModel) {
            // Coroutine implementation for updating cart item
        }

        suspend fun removeFromCart(itemId: String) {
            // Coroutine implementation for removing from cart
        }

        suspend fun clearCart() {
            // Coroutine implementation for clearing the cart
        }

        suspend fun getCartItems() {
            // Coroutine implementation for getting cart items
        }
    }

