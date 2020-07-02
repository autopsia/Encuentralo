package com.sectordefectuoso.encuentralo.data.model

import com.google.firebase.firestore.DocumentId
import java.util.*

data class Chat (
    @DocumentId
    val documentId: String,
    val message: String,
    val dateCreated: Date,
    val author: String
){
}