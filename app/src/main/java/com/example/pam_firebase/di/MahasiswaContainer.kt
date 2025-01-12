package com.example.pam_firebase.di

import android.content.Context
import com.example.pam_firebase.repository.MhsRepository
import com.example.pam_firebase.repository.NetworkRepositoryMhs
import com.google.firebase.firestore.FirebaseFirestore

interface InterfaceContainerApp {
    val MhsRepository: MhsRepository
}

class MahasiswaContainer(private val context: Context) : InterfaceContainerApp {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    override val MhsRepository: MhsRepository by lazy {
        NetworkRepositoryMhs(firestore)
    }
}

