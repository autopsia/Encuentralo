package com.sectordefectuoso.encuentralo.data.model

import android.text.Editable
import com.google.firebase.firestore.DocumentId
import java.util.*

data class ChatMessage(
    @DocumentId
    val documentId: String = "",
    var message: String = "",
    val dateCreated: Date = Date(),
    val author: String = "",
    val type: Int = 1
){
}