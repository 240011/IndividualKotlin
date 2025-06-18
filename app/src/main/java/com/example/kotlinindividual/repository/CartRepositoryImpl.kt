//package com.example.kotlinindividual.repository
//
//import com.cloudinary.Cloudinary
//import com.example.kotlinindividual.model.CartItemModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import kotlinx.coroutines.suspendCancellableCoroutine
//import kotlin.coroutines.resume
//
//class CartRepositoryImpl : CartRepository {
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
//    private val cartRef: DatabaseReference = database.reference.child("carts")
//
//    private val cloudinary = Cloudinary(
//        mapOf(
//            "cloud_name" to "ddmvupmkd",
//            "api_key" to "793911756523935",
//            "api_secret" to "i6_voS7xz1BmNgBE_QhiAkHcugY"
//        )
//    )
//
//    override fun addToCart(cartItem: CartItemModel, callback: (Boolean, String) -> Unit) {
//        val userCartRef = getUser CartRef() ?: run {
//            callback(false, "User  not authenticated")
//            return
//        }
//
//        // Generate a unique ID for the cart item if not provided
//        val itemId = if (cartItem.itemId.isEmpty()) {
//            userCartRef.push().key ?: ""
//        } else {
//            cartItem.itemId
//        }
//
//        if (itemId.isEmpty()) {
//            callback(false, "Failed to generate item ID")
//            return
//        }
//
//        // Check if item already exists in cart
//        userCartRef.child(itemId).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    // Item exists, update quantity
//                    val existingItem = snapshot.getValue(CartItemModel::class.java)
//                    if (existingItem != null) {
//                        val updatedItem = existingItem.copy(
//                            quantity = existingItem.quantity + cartItem.quantity
//                        )
//                        userCartRef.child(itemId).setValue(updatedItem)
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    callback(true, "Cart updated successfully")
//                                } else {
//                                    callback(false, task.exception?.message ?: "Failed to update cart")
//                                }
//                            }
//                    }
//                } else {
//                    // New item, add to cart
//                    val newCartItem = cartItem.copy(itemId = itemId)
//                    userCartRef.child(itemId).setValue(newCartItem)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                callback(true, "Item added to cart successfully")
//                            } else {
//                                callback(false, task.exception?.message ?: "Failed to add item to cart")
//                            }
//                        }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                callback(false, error.message)
//            }
//        })
//    }
//
//    override fun updateCartItem(cartItem: CartItemModel, callback: (Boolean, String) -> Unit) {
//        val userCartRef = getUser CartRef() ?: run {
//            callback(false, "User  not authenticated")
//            return
//        }
//
//        if (cartItem.itemId.isEmpty()) {
//            callback(false, "Invalid item ID")
//            return
//        }
//
//        userCartRef.child(cartItem.itemId).setValue(cartItem)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    callback(true, "Cart item updated successfully")
//                } else {
//                    callback(false, task.exception?.message ?: "Failed to update cart item")
//                }
//            }
//    }
//
//    override fun removeFromCart(itemId: String, callback: (Boolean, String) -> Unit) {
//        val userCartRef = getUser CartRef() ?: run {
//            callback(false, "User  not authenticated")
//            return
//        }
//
//        if (itemId.isEmpty()) {
//            callback(false, "Invalid item ID")
//            return
//        }
//
//        userCartRef.child(itemId).removeValue()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    callback(true, "Item removed from cart successfully")
//                } else {
//                    callback(false, task.exception?.message ?: "Failed to remove item from cart")
//                }
//            }
//    }
//
//    override fun clearCart(callback: (Boolean, String) -> Unit) {
//        val userCartRef = getUser CartRef() ?: run {
//            callback(false, "User  not authenticated")
//            return
//        }
//
//        userCartRef.removeValue()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    callback(true, "Cart cleared successfully")
//                } else {
//                    callback(false, task.exception?.message ?: "Failed to clear cart")
//                }
//            }
//    }
//
//    override fun getCartItems(callback: (List<CartItemModel>?, Boolean, String) -> Unit) {
//        val userCartRef = getUser CartRef() ?: run {
//            callback(null, false, "User  not authenticated")
//            return
//        }
//
//        userCartRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val cartItems = mutableListOf<CartItemModel>()
//
//                for (itemSnapshot in snapshot.children) {
//                    val cartItem = itemSnapshot.getValue(CartItemModel::class.java)
//                    if (cartItem != null) {
//                        cartItems.add(cartItem)
//                    }
//                }
//
//                callback(cartItems, true, "Cart items fetched successfully")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                callback(null, false, error.message)
//            }
//        })
//    }
//
//    override fun getCartItemCount(callback: (Int, Boolean, String) -> Unit) {
//        val userCartRef = getUser CartRef() ?: run {
//            callback(0, false, "User  not authenticated")
//            return
//        }
//
//        userCartRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                var totalCount = 0
//
//                for (itemSnapshot in snapshot.children) {
//                    val cartItem = itemSnapshot.getValue(CartItemModel::class.java)
//                    if (cartItem != null) {
//                        totalCount += cartItem.quantity
//                    }
//                }
//
//                callback(totalCount, true, "Cart count fetched successfully")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                callback(0, false, error.message)
//            }
//        })
//    }
//
//    override suspend fun addToCart(cartItem: CartItemModel): Boolean {
//        return suspendCancellableCoroutine { continuation ->
//            addToCart(cartItem) { success, message ->
//                continuation.resume(success)
//            }
//        }
//    }
//
//    override suspend fun updateCartItem(cartItem: CartItemModel): Boolean {
//        return suspendCancellableCoroutine { continuation ->
//            updateCartItem(cartItem) { success, message ->
//                continuation.resume(success)
//            }
//        }
//    }
//
//    override suspend fun removeFromCart(itemId: String): Boolean {
//        return suspendCancellableCoroutine { continuation ->
//            removeFromCart(itemId) { success, message ->
//                continuation.resume(success)
//            }
//        }
//    }
//
//    override suspend fun clearCart(): Boolean {
//        return suspendCancellableCoroutine { continuation ->
//            clearCart { success, message ->
//                continuation.resume(success)
//            }
//        }
//    }
//
//    override suspend fun getCartItems(): List<CartItemModel>? {
//        return suspendCancellableCoroutine { continuation ->
//            getCartItems { items, success, message ->
//                if (success) {
//                    continuation.resume(items)
//                } else {
//                    continuation.resume(null)
//                }
//            }
//        }
//    }
//}
