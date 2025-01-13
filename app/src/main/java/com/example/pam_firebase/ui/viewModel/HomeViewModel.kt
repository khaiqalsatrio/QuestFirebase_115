package com.example.pam_firebase.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pam_firebase.model.Mahasiswa
import com.example.pam_firebase.repository.MhsRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repoMhs: MhsRepository
) : ViewModel() {

    // State untuk UI
    var mhsUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getMhs() // Memuat data awal
    }

    // Fungsi untuk menghapus data mahasiswa
    fun deleteMhs(mahasiswa: Mahasiswa) {
        viewModelScope.launch {
            // Set status ke Loading atau Delete ketika penghapusan dimulai
            mhsUiState = HomeUiState.Delete

            try {
                // Panggil repo untuk menghapus mahasiswa
                repoMhs.deleteMhs(mahasiswa)

                // Setelah penghapusan berhasil, muat ulang data
                getMhs()

            } catch (e: Exception) {
                // Jika ada error, perbarui UI dengan error message
                mhsUiState = HomeUiState.Error(e.localizedMessage ?: "Terjadi kesalahan saat menghapus data.")
                e.printStackTrace() // Logging error
            }
        }
    }

    fun getMhs() {
        viewModelScope.launch {
            repoMhs.getAllMhs()
                .onStart {
                    mhsUiState = HomeUiState.Loading
                    println("Fetching data...") // Logging untuk debugging
                }
                .catch { e ->
                    // Menangkap exception dan mengubahnya menjadi string pesan kesalahan
                    val errorMessage = e.localizedMessage ?: "Terjadi kesalahan yang tidak diketahui"
                    mhsUiState = HomeUiState.Error(errorMessage)
                    e.printStackTrace() // Logging error
                }
                .collect { data ->
                    mhsUiState = if (data.isEmpty()) {
                        HomeUiState.Empty // Tampilkan status kosong jika data tidak ada
                    } else {
                        HomeUiState.Success(data) // Tampilkan data mahasiswa jika berhasil
                    }
                }
        }
    }
}

// State untuk UI yang menggambarkan status loading, sukses, error, dan lainnya
sealed class HomeUiState {
    object Loading : HomeUiState() // Status sedang memuat data
    data class Success(val data: List<Mahasiswa>) : HomeUiState() // Data mahasiswa berhasil dimuat
    data class Error(val errorMessage: String) : HomeUiState() // Terjadi error saat memuat atau menghapus data
    object Delete : HomeUiState() // Status ketika data sedang dihapus
    object Empty : HomeUiState() // Status ketika tidak ada data yang ditemukan
}
