package com.example.biblioteca.DAO

import androidx.room.*
import com.example.biblioteca.Model.Autor
import kotlinx.coroutines.flow.Flow

@Dao
interface AutorDao {
    @Query("SELECT * FROM autores")
    fun getAllAutores(): Flow<List<Autor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutor(autor: Autor)

    @Update
    suspend fun updateAutor(autor: Autor)

    @Delete
    suspend fun deleteAutor(autor: Autor)
}
