package com.example.myfirst.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfirst.mvvm.DisciplineMVVM

class DisciplineCustomFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisciplineMVVM::class.java)) {
            // Ваша логика создания DisciplineMVVM, возможно, с использованием someParameter
            return DisciplineMVVM(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}