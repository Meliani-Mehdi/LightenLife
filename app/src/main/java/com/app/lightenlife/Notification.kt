package com.app.lightenlife

data class Notification(
    val authId: String,
    val message: String,
    val timeStamp: Map<String, String>, // Firebase timestamp
    val read: Boolean
)