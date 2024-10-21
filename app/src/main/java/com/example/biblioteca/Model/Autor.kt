package com.example.biblioteca.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "autores")
data class Autor(
    @PrimaryKey(autoGenerate = true) val autor_id: Int = 0,
    val nombre: String,
    val apellido: String,
    val nacionalidad: String
)
