package com.sectordefectuoso.encuentralo.data.model

import com.google.firebase.firestore.DocumentId

data class Category(
    @DocumentId
    var documentId:String = "",
    var name:String = "",
    var color:String = "",
    var imgSrc: String = "",
    var position:Int = 0,
    var subCategories: List<SubCategories> = listOf()
)

data class SubCategories(
    @DocumentId
    var documentId: String = "",
    var name: String = "",
    var position: Int = 0
)