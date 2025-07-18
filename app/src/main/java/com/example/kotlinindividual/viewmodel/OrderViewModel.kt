
package com.example.kotlinindividual.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinindividual.model.OrderModel
import com.example.kotlinindividual.repository.OrderRepository


class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _orders = MutableLiveData<List<OrderModel>>()
    val orders: LiveData<List<OrderModel>> get() = _orders

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Load all orders (not just by user)
    fun loadAllOrders() {
        orderRepository.getAllOrders { list, success, message ->
            if (success) {
                _orders.postValue(list)
                _error.postValue(null)
            } else {
                _error.postValue(message)
            }
        }
    }

    fun placeOrder(order: OrderModel) {
        orderRepository.placeOrder(order) { success, message ->
            if (success) {
                loadAllOrders() // Refresh after placing order
            } else {
                _error.postValue(message)
            }
        }
    }

    fun cancelOrder(orderId: String) {
        orderRepository.cancelOrder(orderId) { success, message ->
            if (success) {
                loadAllOrders() // Refresh after cancel
            } else {
                _error.postValue(message)
            }
        }
    }

    fun clearError() {
        _error.postValue(null)
    }
}