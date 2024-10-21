package com.example.biblioteca.ViewModel

import androidx.lifecycle.*
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Repository.LibroRepository
import kotlinx.coroutines.launch

class LibroViewModel(private val repository: LibroRepository) : ViewModel() {
    val allLibros: LiveData<List<Libro>> = repository.allLibros.asLiveData()

    fun insert(libro: Libro) = viewModelScope.launch {
        repository.insert(libro)
    }

    fun update(libro: Libro) = viewModelScope.launch {
        repository.update(libro)
    }

    fun delete(libro: Libro) = viewModelScope.launch {
        repository.delete(libro)
    }
}
