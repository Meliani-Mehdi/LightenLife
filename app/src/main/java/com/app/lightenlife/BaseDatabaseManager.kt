package com.app.lightenlife
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

abstract class BaseDatabaseManager<T : Any>(private val collectionName: String) {
    protected open val db = FirebaseFirestore.getInstance()
    protected val collectionRef = db.collection(collectionName)

    // Add or update a document
    suspend fun addOrUpdateDocument(id: String, data: T) {
        collectionRef.document(id).set(data, SetOptions.merge()).await()
    }

    // Get a document by ID
    suspend fun getDocument(id: String): T? {
        val document = collectionRef.document(id).get().await()
        return document.toObject(getType())
    }

    // Delete a document
    suspend fun deleteDocument(id: String) {
        collectionRef.document(id).delete().await()
    }

    // Abstract method to get the class type
    abstract fun getType(): Class<T>
}