package com.example.biblioteca.ViewModel

import androidx.lifecycle.*
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.Repository.AutorRepository
import kotlinx.coroutines.launch

class AutorViewModel(private val repository: AutorRepository) : ViewModel() {
    val allAutores: LiveData<List<Autor>> = repository.allAutores.asLiveData()

    fun insert(autor: Autor) = viewModelScope.launch {
        if (autor.nombre.isNotEmpty() && autor.apellido.isNotEmpty() && autor.nacionalidad.isNotEmpty()) {
            repository.insert(autor)
        } else {
            // Manejar error: mostrar mensaje al usuario
        }
    }

    fun update(autor: Autor) = viewModelScope.launch {
        repository.update(autor)
    }

    fun delete(autor: Autor) = viewModelScope.launch {
        repository.delete(autor)
    }
}
