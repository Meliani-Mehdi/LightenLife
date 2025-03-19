package com.app.lightenlife

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UserManager {
    private val db = FirebaseFirestore.getInstance()

    // Add or update a client
    fun addOrUpdateClient(authId: String, client: Client, onComplete: (Boolean) -> Unit) {
        db.collection("users").document("clients").collection("clients").document(authId)
            .set(client, SetOptions.merge())
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.w("UsersManager", "Failed to add/update client", e)
                onComplete(false)
            }
    }

    // Add or update a doctor
    fun addOrUpdateDoctor(authId: String, doctor: Doctor, onComplete: (Boolean) -> Unit) {
        db.collection("users").document("doctors").collection("doctors").document(authId)
            .set(doctor, SetOptions.merge())
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.w("UsersManager", "Failed to add/update doctor", e)
                onComplete(false)
            }
    }

    fun getUser(authId: String, onComplete: (Any?) -> Unit) {
        // Check if the user is a client
        db.collection("users").document("clients").collection("clients").document(authId).get()
            .addOnSuccessListener { clientDocument ->
                if (clientDocument.exists()) {
                    val client = clientDocument.toObject(Client::class.java)
                    onComplete(client)
                } else {
                    // If not a client, check if the user is a doctor
                    db.collection("users").document("doctors").collection("doctors").document(authId).get()
                        .addOnSuccessListener { doctorDocument ->
                            if (doctorDocument.exists()) {
                                val doctor = doctorDocument.toObject(Doctor::class.java)
                                onComplete(doctor)
                            } else {
                                // If neither, return null
                                onComplete(null)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.w("UsersManager", "Failed to fetch doctor", e)
                            onComplete(null)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w("UsersManager", "Failed to fetch client", e)
                onComplete(null)
            }
    }
}