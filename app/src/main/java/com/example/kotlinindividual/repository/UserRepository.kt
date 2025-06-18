package com.example.kotlinindividual.repository

import com.example.kotlinindividual.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    //login
    //register
    //forgetPassword
    //updateProfile
    //getCurrentUser
    //getUserById
    //addUserToDatabase
    //logout
    fun login(email:String,password:String,
              callback:(Boolean,String)-> Unit)
    fun register(email:String,password:String,
                 callback:(Boolean,String,String)-> Unit)
    fun forgetPassword(email: String,callback: (Boolean, String) -> Unit)
    fun updateProfile(userId: String,data:MutableMap<String,Any?>,callback: (Boolean, String) -> Unit)
    fun getCurrentUser():FirebaseUser?
    fun getUserById(userId: String,callback: (UserModel?,Boolean, String) -> Unit)
    //dataBase Function
    fun addUserToDatabase(userId:String,model: UserModel,callback: (Boolean, String) -> Unit)
    fun logout(callback: (Boolean, String) -> Unit)
}