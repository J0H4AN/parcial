package com.example.biblioteca.ViewModel

import androidx.lifecycle.*
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Repository.MiembroRepository
import kotlinx.coroutines.launch

class MiembroViewModel(private val repository: MiembroRepository) : ViewModel() {
    val allMiembros: LiveData<List<Miembro>> = repository.allMiembros.asLiveData()

    fun insert(miembro: Miembro) = viewModelScope.launch {
        repository.insert(miembro)
    }

    fun update(miembro: Miembro) = viewModelScope.launch {
        repository.update(miembro)
    }

    fun delete(miembro: Miembro) = viewModelScope.launch {
        repository.delete(miembro)
    }
}
