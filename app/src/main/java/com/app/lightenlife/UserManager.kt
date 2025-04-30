package com.app.lightenlife

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UserManager {
    private val db = FirebaseFirestore.getInstance()

    // Add or update a client at /clients/{userId}
    fun addOrUpdateClient(authId: String, client: Client, onComplete: (Boolean) -> Unit) {
        db.collection("clients").document(authId)
            .set(client, SetOptions.merge())
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.w("UserManager", "Failed to add/update client", e)
                onComplete(false)
            }
    }

    // Add or update a doctor at /doctors/{userId}
    fun addOrUpdateDoctor(authId: String, doctor: Doctor, onComplete: (Boolean) -> Unit) {
        db.collection("doctors").document(authId)
            .set(doctor, SetOptions.merge())
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.w("UserManager", "Failed to add/update doctor", e)
                onComplete(false)
            }
    }

    // Get user from /clients/{userId} or fallback to /doctors/{userId}
    fun getUser(authId: String, onComplete: (Any?) -> Unit) {
        db.collection("clients").document(authId).get()
            .addOnSuccessListener { clientDocument ->
                if (clientDocument.exists()) {
                    Log.d("UserManager", "Client doc exists: ${clientDocument.data}")
                    val client = clientDocument.toObject(Client::class.java)
                    Log.d("UserManager", "Parsed client: $client")
                    onComplete(client)
                } else {
                    db.collection("doctors").document(authId).get()
                        .addOnSuccessListener { doctorDocument ->
                            if (doctorDocument.exists()) {
                                val doctor = doctorDocument.toObject(Doctor::class.java)
                                onComplete(doctor)
                            } else {
                                onComplete(null)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.w("UserManager", "Failed to fetch doctor", e)
                            onComplete(null)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w("UserManager", "Failed to fetch client", e)
                onComplete(null)
            }
    }
}
