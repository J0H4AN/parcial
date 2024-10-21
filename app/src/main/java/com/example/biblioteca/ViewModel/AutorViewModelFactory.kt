package com.example.biblioteca.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.biblioteca.Repository.AutorRepository

class AutorViewModelFactory(private val repository: AutorRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AutorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AutorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
