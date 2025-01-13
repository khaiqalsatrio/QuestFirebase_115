package com.example.pam_firebase.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pam_firebase.R
import com.example.pam_firebase.model.Mahasiswa
import com.example.pam_firebase.ui.viewModel.HomeUiState
import com.example.pam_firebase.ui.viewModel.HomeViewModel
import com.example.pam_firebase.ui.viewModel.PenyediaViewModel

@Composable
fun CardMhs(
    onDelete: (Mahasiswa) -> Unit = {},
    mhs: Mahasiswa,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = { },
    onDetailClick: (String) -> Unit = {},

) {
    Card(
        onClick = { onClick(mhs.nim) },  // Make sure onClick handles nim properly
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.AccountBox, contentDescription = "")
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = mhs.judulSkripsi,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = mhs.nim,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.padding(4.dp))
                IconButton(onClick = {
                    onDelete(mhs)
                }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "")
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = mhs.dosBim1,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToDetail: (String) -> Unit, // Tambahkan fungsi navigasi ke detail
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Daftar Mahasiswa") },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Mahasiswa")
            }
        }
    ) { innerPadding ->
        HomeStatus(
            homeUiState = viewModel.mhsUiState,
            retryAction = { viewModel.getMhs() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = navigateToDetail, // Navigasikan ke detail
            onDeleteClick = { viewModel.deleteMhs(it) }
        )
    }
}


@Composable
fun HomeStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit = {}, // Accept onDeleteClick parameter
    onDetailClick: (String) -> Unit
) {
    var deleteConfirmationRequired by rememberSaveable {
        mutableStateOf<Mahasiswa?>(null)
    }

    when (homeUiState) {
        is HomeUiState.Loading -> {
            OnLoading(modifier = modifier.fillMaxSize())
        }
        is HomeUiState.Success -> {
            ListMahasiswa(
                listMhs = homeUiState.data,
                modifier = modifier.fillMaxWidth(),
                onClick = { nim -> onDetailClick(nim) },
                onDelete = { mhs ->
                    deleteConfirmationRequired = mhs // Set the mahasiswa to confirm delete
                }
            )

            // Show the delete confirmation dialog when required
            deleteConfirmationRequired?.let { data ->
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        onDeleteClick(data) // Call the delete function
                        deleteConfirmationRequired = null // Reset after deletion
                    },
                    onDeleteCancel = {
                        deleteConfirmationRequired = null // Reset if canceled
                    }
                )
            }
        }
        is HomeUiState.Error -> {
            OnError(
                message = homeUiState.errorMessage ?: "Terjadi kesalahan.",
                retryAction = retryAction,
                modifier = modifier.fillMaxSize()
            )
        }

        HomeUiState.Delete -> {
            // Tampilkan UI sederhana atau logika pemberitahuan
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Data berhasil dihapus.", style = MaterialTheme.typography.bodyLarge)
            }
        }

        HomeUiState.Empty -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_connection_error), // Ilustrasi kosong
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tidak ada data tersedia.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

    }

}

@Composable
fun ListMahasiswa(
    listMhs: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = { },
    onDelete: (Mahasiswa) -> Unit = { }
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = listMhs,
            itemContent = { mhs ->
                CardMhs(
                    onDelete = { onDelete(mhs)},
                    mhs = mhs,
                    onClick = { onClick(mhs.nim) } // Pass the nim to onClick
                )
            }
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDeleteCancel,
        title = { Text("Delete Data") },
        text = { Text("Are you sure you want to delete this data?") },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text("Cancel")
            }
        },
        modifier = modifier.padding(16.dp) // Menambahkan padding agar tidak terlalu rapat
    )
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.icon_loading),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun OnError(
    message: String,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier)
{
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = null, // Tambahkan contentDescription untuk aksesibilitas
            modifier = Modifier.size(100.dp) // Opsional, tambahkan ukuran jika diperlukan
        )
        Text(
            text = message,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text("Coba Lagi")
        }
    }
}
