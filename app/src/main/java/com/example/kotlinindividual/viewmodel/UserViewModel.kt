package com.example.kotlinindividual.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinindividual.model.UserModel
import com.example.kotlinindividual.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel(val repo:UserRepository) :ViewModel(){
    fun login(email:String,password:String,
              callback:(Boolean,String)-> Unit){
        repo.login(email,password,callback)
    }
    fun register(email:String,password:String,
                 callback:(Boolean,String,String)-> Unit){
        repo.register(email, password, callback)
    }

    fun forgetPassword(email: String,callback: (Boolean, String) -> Unit){
        repo.forgetPassword(email,callback)
    }
    fun updateProfile(userId: String,data:MutableMap<String,Any?>,callback: (Boolean, String) -> Unit){
        repo.updateProfile(userId,data,callback)
    }
    fun getCurrentUser(): FirebaseUser?{

      return repo.getCurrentUser()
    }

    private val _users = MutableLiveData<UserModel?>()

    val users: LiveData<UserModel?> get() = _users

    fun getUserById(
        userId: String,
    ) {
        repo.getUserById(userId) {
                users,success,message->
            if(success){
                _users.postValue(users)
            }
        }
    }


    //dataBase Function
    fun addUserToDatabase(userId:String, model: UserModel, callback: (Boolean, String) -> Unit){
        repo.addUserToDatabase(userId,model,callback)
    }
    fun logout(callback: (Boolean, String) -> Unit){
        repo.logout(callback)
    }
}
