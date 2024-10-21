package com.example.biblioteca.DAO

import androidx.room.*
import com.example.biblioteca.Model.Miembro
import kotlinx.coroutines.flow.Flow

@Dao
interface MiembroDao {
    @Query("SELECT * FROM miembros")
    fun getAllMiembros(): Flow<List<Miembro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMiembro(miembro: Miembro)

    @Update
    suspend fun updateMiembro(miembro: Miembro)

    @Delete
    suspend fun deleteMiembro(miembro: Miembro)
}
