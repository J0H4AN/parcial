package com.example.biblioteca.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.biblioteca.Repository.MiembroRepository

class MiembroViewModelFactory(private val repository: MiembroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MiembroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MiembroViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
