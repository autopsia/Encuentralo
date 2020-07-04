package com.sectordefectuoso.encuentralo.data.model

data class Agreement(
    val emitter: String = "",
    val receiver: String = "",
    val price: Double = 0.0,
    var isAccepted : Int = 0
//0 no hay respuesta 1 aprovado 2 rechazado
)