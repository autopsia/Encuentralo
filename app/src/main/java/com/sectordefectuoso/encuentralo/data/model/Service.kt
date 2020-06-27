package com.sectordefectuoso.encuentralo.data.model

import com.google.firebase.firestore.DocumentId

data class Service (
    @DocumentId
    var documentId: String = "",
    var title: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var status: Boolean = true,
    var userId: String = ""
)