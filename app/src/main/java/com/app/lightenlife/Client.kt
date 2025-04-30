package com.app.lightenlife

data class Client(
    val name: String = "",
    val userName: String = "",
    val info: Map<String, List<InfoEntry>> = emptyMap()
)
