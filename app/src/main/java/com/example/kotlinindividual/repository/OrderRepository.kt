package com.example.kotlinindividual.repository


import com.example.kotlinindividual.model.OrderModel

interface OrderRepository {
    fun placeOrder(order: OrderModel, callback: (Boolean, String) -> Unit)
    fun getOrdersByUser(userId: String, callback: (List<OrderModel>, Boolean, String) -> Unit)
    fun cancelOrder(orderId: String, callback: (Boolean, String) -> Unit)
    fun getAllOrders(callback: (List<OrderModel>, Boolean, String) -> Unit)
}
