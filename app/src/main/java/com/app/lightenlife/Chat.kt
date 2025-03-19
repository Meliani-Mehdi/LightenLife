package com.app.lightenlife

data class Chat(
    val authIdDoctor: String,
    val authIdClient: String,
    val messages: List<Message>
)