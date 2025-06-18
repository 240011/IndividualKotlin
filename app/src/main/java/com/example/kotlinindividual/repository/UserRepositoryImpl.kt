package com.example.kotlinindividual.repository

import com.example.kotlinindividual.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepositoryImpl:UserRepository {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref: DatabaseReference = database.reference.child("users")

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "login Success")
                } else {
                    callback(false, it.exception?.message.toString())
                }

            }
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(
                        true, "Register Success",
                        "${auth.currentUser?.uid}"
                    )
                } else {
                    callback(false, "${it.exception?.message}", "")
                }
            }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(
                        true, "Reset email send to $email"
                    )
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }
    }

    override fun updateProfile(
        userId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).updateChildren(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "User  profile updated")
            } else {
                callback(false, "${task.exception?.message}")
            }
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getUserById(userId: String, callback: (UserModel?, Boolean, String) -> Unit) {
        ref.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val users = snapshot.getValue(UserModel::class.java)
                    if (users != null) {
                        callback(users, true, "data fetched")

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,error.message)
            }
        })
    }


            override fun addUserToDatabase(
                userId: String,
                model: UserModel,
                callback: (Boolean, String) -> Unit
            ) {
                ref.child(userId).setValue(model).addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback(true, "User added to database")
                    } else {
                        callback(false, "${it.exception?.message}")
                    }
                }

            }

            override fun logout(callback: (Boolean, String) -> Unit) {
                try {
                    auth.signOut()
                    callback(true, "logout successfully")
                } catch (e: Exception) {
                    callback(false, "${e.message}")
                }
            }
        }
