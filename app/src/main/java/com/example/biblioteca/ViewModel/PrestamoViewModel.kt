package com.example.biblioteca.ViewModel

import androidx.lifecycle.*
import com.example.biblioteca.Model.Prestamo
import com.example.biblioteca.Repository.PrestamoRepository
import kotlinx.coroutines.launch

class PrestamoViewModel(private val repository: PrestamoRepository) : ViewModel() {
    val allPrestamos: LiveData<List<Prestamo>> = repository.allPrestamos.asLiveData()

    fun insert(prestamo: Prestamo) = viewModelScope.launch {
        repository.insert(prestamo)
    }

    fun update(prestamo: Prestamo) = viewModelScope.launch {
        repository.update(prestamo)
    }

    fun delete(prestamo: Prestamo) = viewModelScope.launch {
        repository.delete(prestamo)
    }
}

