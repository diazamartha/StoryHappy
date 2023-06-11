package com.example.storyhappy.di

import com.example.storyhappy.presentation.detail.DetailViewModel
import com.example.storyhappy.presentation.login.LoginViewModel
import com.example.storyhappy.presentation.main.MainViewModel
import com.example.storyhappy.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { DetailViewModel(get()) }
}