package com.example.mobilecrud

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val telephone: String,
    val adresse: String,
    val ville: String,
    val codePostal: String,
    val pays: String,
    val dateNaissance: String,
    val genre: String,
    val photo: String,
    val role: String
)