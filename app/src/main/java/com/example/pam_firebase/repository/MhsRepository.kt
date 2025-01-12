package com.example.pam_firebase.repository

import com.example.pam_firebase.model.Mahasiswa
import kotlinx.coroutines.flow.Flow

interface MhsRepository {

    suspend fun insertMhs(mahasiswa: Mahasiswa)

    fun getAllMhs(): Flow<List<Mahasiswa>>

    fun getMhs(nim: String): Flow<Mahasiswa>

    suspend fun deleteMhs(mahasiswa: Mahasiswa)

    suspend fun updateMhs(mahasiswa:Mahasiswa)

}