package com.sectordefectuoso.encuentralo.data.model

import com.google.firebase.firestore.DocumentId
import java.util.*

data class Post(
    @DocumentId
    val documentId: String,
    val dateCreated: Date,
    val description: String,
    val title: String,
    val user: User

) {
}