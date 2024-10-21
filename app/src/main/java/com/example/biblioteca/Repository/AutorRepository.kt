package com.example.biblioteca.Repository

import com.example.biblioteca.DAO.AutorDao
import com.example.biblioteca.Model.Autor
import kotlinx.coroutines.flow.Flow

class AutorRepository(private val autorDao: AutorDao) {
    val allAutores: Flow<List<Autor>> = autorDao.getAllAutores()

    suspend fun insert(autor: Autor) {
        autorDao.insertAutor(autor)
    }

    suspend fun update(autor: Autor) {
        autorDao.updateAutor(autor)
    }

    suspend fun delete(autor: Autor) {
        autorDao.deleteAutor(autor)
    }
}
