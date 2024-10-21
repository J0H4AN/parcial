package com.example.biblioteca.Repository

import com.example.biblioteca.DAO.MiembroDao
import com.example.biblioteca.Model.Miembro
import kotlinx.coroutines.flow.Flow

class MiembroRepository(private val miembroDao: MiembroDao) {
    val allMiembros: Flow<List<Miembro>> = miembroDao.getAllMiembros()

    suspend fun insert(miembro: Miembro) {
        miembroDao.insertMiembro(miembro)
    }

    suspend fun update(miembro: Miembro) {
        miembroDao.updateMiembro(miembro)
    }

    suspend fun delete(miembro: Miembro) {
        miembroDao.deleteMiembro(miembro)
    }
}
