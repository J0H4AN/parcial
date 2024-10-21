package com.example.biblioteca.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prestamos")
data class Prestamo(
    @PrimaryKey(autoGenerate = true) val prestamo_id: Int = 0,
    val libro_id: Int,
    val miembro_id: Int,
    val fecha_prestamo: String,  // Usar String para representar la fecha en formato DATE
    val fecha_devolucion: String? = null  // También como String para la fecha de devolución
)
