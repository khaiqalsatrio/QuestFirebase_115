package com.example.pam_firebase.ui.viewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pam_firebase.MahasiswaApp

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                MahasiswaApp().containerApp.MhsRepository
            )
        }
        initializer {
            InsertViewModel (
                MahasiswaApp().containerApp.MhsRepository
            )
        }
        initializer {
            DetailViewModel (
                MahasiswaApp().containerApp.MhsRepository
            )
        }
    }
}

fun CreationExtras.MahasiswaApp(): MahasiswaApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MahasiswaApp)