package com.example.kotlinindividual.model
    data class CartItemModel(
    val itemId: String,        // Unique identifier for the item
    val name: String,          // Name of the item
    val price: Double,         // Price of the item
    val quantity: Int,         // Quantity of the item in the cart
    val imageUrl: String? = null // Optional image URL for the item
)
