package com.example.pam_firebase

import android.app.Application
import com.example.pam_firebase.di.MahasiswaContainer

class MahasiswaApp : Application() {

    // Menyimpan instance dari ContainerApp sebagai properti aplikasi
    lateinit var containerApp: MahasiswaContainer

    // Dipanggil ketika aplikasi pertama kali dijalankan
    override fun onCreate() {
        super.onCreate()
        // Membuat instance dari ContainerApp dan menyimpannya di properti containerApp
        containerApp = MahasiswaContainer(this)
        // Instance adalah objek yang dibuat dari class ContainerApp untuk mengelola dependensi aplikasi
    }
}