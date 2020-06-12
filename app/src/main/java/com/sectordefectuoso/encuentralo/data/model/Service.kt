package com.sectordefectuoso.encuentralo.data.model

class Service {
    var name: String = ""
    var description: String = ""
    var status: Boolean = true


    override fun toString(): String {
        return "Service(name='$name', description='$description', status=$status)"
    }
}