package com.example.recipeplannerlite.Data

data class Recetas(
    val nombre: String,
    val ingredientes: List<String>,
    var unidad: String = "gr"
)