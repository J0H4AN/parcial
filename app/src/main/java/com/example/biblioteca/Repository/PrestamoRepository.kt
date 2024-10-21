package com.example.biblioteca.Repository

import com.example.biblioteca.DAO.PrestamoDao
import com.example.biblioteca.Model.Prestamo
import kotlinx.coroutines.flow.Flow

class PrestamoRepository(private val prestamoDao: PrestamoDao) {
    val allPrestamos: Flow<List<Prestamo>> = prestamoDao.getAllPrestamos()

    suspend fun insert(prestamo: Prestamo) {
        prestamoDao.insertPrestamo(prestamo)
    }

    suspend fun update(prestamo: Prestamo) {
        prestamoDao.updatePrestamo(prestamo)
    }

    suspend fun delete(prestamo: Prestamo) {
        prestamoDao.deletePrestamo(prestamo)
    }
}
