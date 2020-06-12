package com.sectordefectuoso.encuentralo.data.model

import java.util.*

class User{
    var document: String = ""
    var names: String = ""
    var lastNames: String = ""
    var birthdate: Date = Date()
    var phone: String = ""
    var email: String = ""
    var service: Service? = null

    override fun toString(): String {
        return "User(document='$document', names='$names', lastNames='$lastNames', birthdate=$birthdate, phone='$phone', email='$email', service=$service)"
    }
}