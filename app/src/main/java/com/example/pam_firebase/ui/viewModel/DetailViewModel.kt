package com.example.pam_firebase.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pam_firebase.model.Mahasiswa
import com.example.pam_firebase.repository.MhsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val mahasiswa: Mahasiswa) : DetailUiState()
    data class Error(val errorMessage: String) : DetailUiState()
}

class DetailViewModel(private val repository: MhsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState

    fun getMhs(nim: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                // Mengumpulkan data dari Flow
                val mahasiswa = repository.getMhs(nim).first()
                _uiState.value = DetailUiState.Success(mahasiswa)
            } catch (e: IOException) {
                e.printStackTrace()
                _uiState.value = DetailUiState.Error("Gagal mengambil data: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = DetailUiState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }
}
