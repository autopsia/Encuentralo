package com.sectordefectuoso.encuentralo.data.model

import java.util.*

data class User(
    var documentId: String = "",
    var document: String = "",
    var names: String = "",
    var lastNames: String = "",
    var birthdate: Date = Date(),
    var phone: String = "",
    var email: String = "",
    var service: Service? = null
)