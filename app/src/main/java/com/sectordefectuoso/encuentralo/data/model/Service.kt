package com.sectordefectuoso.encuentralo.data.model

import com.google.firebase.firestore.DocumentId
import java.util.*

data class Service (
    @DocumentId
    var documentId: String = "",
    var title: String = "",
    var description: String = "",
    var status: Boolean = true,
    var createdDate: Date = Date(),
    var subcategoryId: String = "",
    var userId: String = ""
)