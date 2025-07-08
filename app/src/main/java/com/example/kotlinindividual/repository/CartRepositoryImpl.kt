package com.example.kotlinindividual.repository

import com.example.kotlinindividual.model.CartItemModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartRepositoryImpl : CartRepository {

    private val database = FirebaseDatabase.getInstance()
    private val cartRef = database.reference.child("cart")

    override fun addToCart(model: CartItemModel, callback: (Boolean, String) -> Unit) {
        // First check if product already exists in cart
        getCartItemByProductId(model.productId) { existingItem, exists, _ ->
            if (exists && existingItem != null) {
                // Update quantity if item already exists
                val updatedQuantity = existingItem.quantity + model.quantity
                val updateData = mutableMapOf<String, Any?>(
                    "quantity" to updatedQuantity,
                    "totalPrice" to existingItem.productPrice * updatedQuantity
                )
                updateCartItem(existingItem.cartId, updateData) { success, message ->
                    callback(success, if (success) "Cart updated successfully" else message)
                }
            } else {
                // Add new item to cart
                val cartId = cartRef.push().key.toString()
                model.cartId = cartId
                model.calculateTotalPrice()

                cartRef.child(cartId).setValue(model).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(true, "Item added to cart successfully")
                    } else {
                        callback(false, task.exception?.message ?: "Failed to add item to cart")
                    }
                }
            }
        }
    }

    override fun updateCartItem(cartId: String, data: MutableMap<String, Any?>, callback: (Boolean, String) -> Unit) {
        cartRef.child(cartId).updateChildren(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Cart item updated successfully")
            } else {
                callback(false, task.exception?.message ?: "Failed to update cart item")
            }
        }
    }

    override fun removeFromCart(cartId: String, callback: (Boolean, String) -> Unit) {
        cartRef.child(cartId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Item removed from cart successfully")
            } else {
                callback(false, task.exception?.message ?: "Failed to remove item from cart")
            }
        }
    }

    override fun getCartItems(callback: (List<CartItemModel>, Boolean, String) -> Unit) {
        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val cartItems = mutableListOf<CartItemModel>()
                    for (cartSnapshot in snapshot.children) {
                        val cartItem = cartSnapshot.getValue(CartItemModel::class.java)
                        if (cartItem != null) {
                            cartItems.add(cartItem)
                        }
                    }
                    callback(cartItems, true, "Cart items fetched successfully")
                } else {
                    callback(emptyList(), true, "Cart is empty")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList(), false, error.message)
            }
        })
    }

    override fun clearCart(callback: (Boolean, String) -> Unit) {
        cartRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Cart cleared successfully")
            } else {
                callback(false, task.exception?.message ?: "Failed to clear cart")
            }
        }
    }

    override fun getCartItemByProductId(productId: String, callback: (CartItemModel?, Boolean, String) -> Unit) {
        cartRef.orderByChild("productId").equalTo(productId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (cartSnapshot in snapshot.children) {
                            val cartItem = cartSnapshot.getValue(CartItemModel::class.java)
                            if (cartItem != null) {
                                callback(cartItem, true, "Cart item found")
                                return
                            }
                        }
                    }
                    callback(null, false, "Cart item not found")
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null, false, error.message)
                }
            })
    }
}