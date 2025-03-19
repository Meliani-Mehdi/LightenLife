package com.app.lightenlife

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthenticationManager(private val usersManager: UserManager) {
    private val auth = FirebaseAuth.getInstance()

    // Sign up a new user (client or doctor)
    fun signUp(email: String, password: String, user: Any, onComplete: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthManager", "Sign up successful")
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        // Add the user to Firestore based on their type
                        when (user) {
                            is Client -> usersManager.addOrUpdateClient(userId, user) { success ->
                                onComplete(success)
                            }
                            is Doctor -> usersManager.addOrUpdateDoctor(userId, user) { success ->
                                onComplete(success)
                            }
                            else -> {
                                Log.w("AuthManager", "Invalid user type")
                                onComplete(false)
                            }
                        }
                    } else {
                        Log.w("AuthManager", "User ID is null")
                        onComplete(false)
                    }
                } else {
                    Log.w("AuthManager", "Sign up failed", task.exception)
                    onComplete(false)
                }
            }
    }

    // Sign in with email and password
    fun signIn(email: String, password: String, onComplete: (FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthManager", "Sign in successful")
                    onComplete(auth.currentUser)
                } else {
                    Log.w("AuthManager", "Sign in failed", task.exception)
                    onComplete(null)
                }
            }
    }

    // Get the current user's ID
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    // Get a user (Client or Doctor) by authId
    fun getUser(authId: String, onComplete: (Any?) -> Unit) {
        usersManager.getUser(authId, onComplete)
    }

    // Sign out
    fun signOut() {
        auth.signOut()
    }
}