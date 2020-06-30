package com.sectordefectuoso.encuentralo.data.model

import com.google.firebase.firestore.DocumentId
import java.util.*

data class User(
    @DocumentId
    var documentId: String = "",
    var document: String = "",
    var names: String = "",
    var lastNames: String = "",
    var birthdate: Date = Date(),
    var phone: String = "",
    var email: String = "",
    var password: String = "",
    var imageUrl: String = ""
)