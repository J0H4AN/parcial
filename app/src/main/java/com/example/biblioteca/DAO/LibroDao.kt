package com.example.biblioteca.DAO

import androidx.room.*
import com.example.biblioteca.Model.Libro
import kotlinx.coroutines.flow.Flow

@Dao
interface LibroDao {
    @Query("SELECT * FROM libros")
    fun getAllLibros(): Flow<List<Libro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibro(libro: Libro)

    @Update
    suspend fun updateLibro(libro: Libro)

    @Delete
    suspend fun deleteLibro(libro: Libro)
}
