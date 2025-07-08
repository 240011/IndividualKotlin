package com.example.kotlinindividual.model

data class CartItemModel(
    var cartId: String = "",
    var productId: String = "",
    var productName: String = "",
    var productPrice: Double = 0.0,
    var productDescription: String = "",
    var image: String = "",
    var quantity: Int = 1,
    var totalPrice: Double = 0.0
) {
    fun calculateTotalPrice() {
        totalPrice = productPrice * quantity
    }
}