package com.sectordefectuoso.encuentralo.data.model

import com.google.firebase.firestore.DocumentId

data class Category(
    @DocumentId
    var documentId:String = "",
    var name:String = "",
    var color:String = "#FFFFFF",
    var imgSrc: String = "",
    var position:Int = 0,
    var subCategories: List<SubCategory> = listOf()
)

data class SubCategory(
    @DocumentId
    var documentId: String = "",
    var name: String = "",
    var position: Int = 0
)