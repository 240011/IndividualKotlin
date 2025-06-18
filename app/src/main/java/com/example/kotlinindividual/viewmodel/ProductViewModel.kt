package com.example.kotlinindividual.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinindividual.model.ProductModel
import com.example.kotlinindividual.repository.ProductRepository

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit){
        repo.uploadImage(context,imageUri,callback)
    }

    // Function to add a product
    fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        repo.addProduct(model, callback)
    }

    // Function to update a product
    fun updateProduct(productId: String, data: MutableMap<String, Any?>, callback: (Boolean, String) -> Unit) {
        repo.updateProduct(productId, data, callback)
    }

    // Function to delete a product
    fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteProduct(productId, callback)
    }

    // LiveData for a single product
    private val _products = MutableLiveData<ProductModel?>()
    val products: LiveData<ProductModel?> get() = _products

    // LiveData for all products
    private val _allProducts = MutableLiveData<List<ProductModel?>>()
    val allProducts: LiveData<List<ProductModel?>> get() = _allProducts

    private val _loading=MutableLiveData<Boolean>()
    var loading=MutableLiveData<Boolean>()
        get()=_loading

    // Function to get a product by ID
    fun getProductById(productId: String) {
        repo.getProductById(productId) { data, success, msg ->
            if (success) {
                _products.postValue(data)
            } else {
                _products.postValue(null)
            }
        }
    }

    // Function to get all products
    fun getAllProducts() {
        _loading.postValue(true)
        repo.getAllProducts { data, success, msg ->
            if (success) {
                _loading.postValue(false)
                _allProducts.postValue(data)
            } else {
                _loading.postValue(false)
                _allProducts.postValue(emptyList())
            }
        }
    }
}

