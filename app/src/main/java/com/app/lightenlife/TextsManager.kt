package com.app.lightenlife

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class TextsManager : BaseDatabaseManager<Any>("texts") {
    override val db = FirebaseFirestore.getInstance()

    // Add or update a chat
    suspend fun addOrUpdateChat(chatId: String, chat: Chat) {
        db.collection("texts").document("chats").collection("chats").document(chatId)
            .set(chat, SetOptions.merge())
            .await()
    }

    // Add a message to a chat
    suspend fun addMessage(chatId: String, message: Message) {
        db.collection("texts").document("chats").collection("chats").document(chatId)
            .update("messages", FieldValue.arrayUnion(message))
            .await()
    }

    // Get a chat by ID
    suspend fun getChat(chatId: String): Chat? {
        val document = db.collection("texts").document("chats").collection("chats").document(chatId).get().await()
        return document.toObject(Chat::class.java)
    }

    override fun getType(): Class<Any> {
        return Any::class.java
    }
}
