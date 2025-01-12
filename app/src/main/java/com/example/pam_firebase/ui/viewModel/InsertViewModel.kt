package com.example.pam_firebase.ui.viewModel

import com.example.pam_firebase.model.Mahasiswa

sealed class FormState {
    object Idle : FormState()
    object Loading : FormState()
    data class Success(val message: String) : FormState()
    data class Error(val message: String) : FormState()
}

data class InsertUiState (
    val insertUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
)

data class FormErrorState (
    val nim: String? = null,
    val nama: String? = null,
    val gender: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null,
) {
    fun isValid() : Boolean {
        return nim == null && nama == null && gender == null &&
                alamat == null && kelas == null && angkatan == null
    }
}

data class MahasiswaEvent(
    val nim: String = "",
    val nama: String = "",
    val gender: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = "",
)

fun MahasiswaEvent.toMhsModel () : Mahasiswa = Mahasiswa (
    nim = nim,
    nama = nama,
    gender = gender,
    alamat = alamat,
    kelas = kelas,
    angkatan = angkatan
)