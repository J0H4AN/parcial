package com.example.biblioteca.Database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.example.biblioteca.DAO.AutorDao
import com.example.biblioteca.DAO.LibroDao
import com.example.biblioteca.DAO.MiembroDao
import com.example.biblioteca.DAO.PrestamoDao
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Model.Prestamo

@Database(entities = [Libro::class, Autor::class, Miembro::class, Prestamo::class], version = 2)
@TypeConverters(Converters::class)
abstract class BibliotecaDatabase : RoomDatabase() {
    abstract fun libroDao(): LibroDao
    abstract fun autorDao(): AutorDao
    abstract fun miembroDao(): MiembroDao
    abstract fun prestamoDao(): PrestamoDao

    companion object {
        @Volatile
        private var INSTANCE: BibliotecaDatabase? = null

        fun getDatabase(context: Context): BibliotecaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BibliotecaDatabase::class.java,
                    "biblioteca_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}