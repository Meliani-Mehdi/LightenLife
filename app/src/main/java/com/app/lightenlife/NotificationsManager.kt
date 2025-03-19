package com.app.lightenlife
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class NotificationsManager : BaseDatabaseManager<Any>("notifications") {
    override val db = FirebaseFirestore.getInstance()

    // Add or update a notification
    suspend fun addOrUpdateNotification(notificationId: String, notification: Notification) {
        db.collection("notifications").document(notificationId)
            .set(notification, SetOptions.merge())
            .await()
    }

    suspend fun getNotification(notificationId: String): Notification? {
        val document = db.collection("notifications").document(notificationId).get().await()
        return document.toObject(Notification::class.java)
    }

    override fun getType(): Class<Any> {
        return Any::class.java
    }
}
