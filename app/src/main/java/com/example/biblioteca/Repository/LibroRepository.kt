package com.example.biblioteca.Repository

import com.example.biblioteca.DAO.LibroDao
import com.example.biblioteca.Model.Libro
import kotlinx.coroutines.flow.Flow

class LibroRepository(private val libroDao: LibroDao) {
    val allLibros: Flow<List<Libro>> = libroDao.getAllLibros()

    suspend fun insert(libro: Libro) {
        libroDao.insertLibro(libro)
    }

    suspend fun update(libro: Libro) {
        libroDao.updateLibro(libro)
    }

    suspend fun delete(libro: Libro) {
        libroDao.deleteLibro(libro)
    }
}
