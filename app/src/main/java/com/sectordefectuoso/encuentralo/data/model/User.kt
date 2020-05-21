package com.sectordefectuoso.encuentralo.data.model

class User(
    email: String,
    password: String,
    document: String,
    names: String,
    lastNames: String,
    birthdate:String
) {
    override fun toString(): String {
        return super.toString()
    }
}