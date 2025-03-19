package com.app.lightenlife

data class Message(
    val isDoctor: Boolean,
    val mes: String,
    val timeStamp: Map<String, String> // Firebase timestamp
)