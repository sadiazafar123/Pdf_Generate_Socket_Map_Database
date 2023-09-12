package com.example.totopartnetapppracticeapplication

import com.example.totopartnetapppracticeapplication.api.Repository
import com.example.totopartnetapppracticeapplication.localdb.LocalRepo
import com.example.totopartnetapppracticeapplication.viewmodel.MyViewModel
import com.example.totopartnetapppracticeapplication.viewmodel.PdfViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single{
      LocalRepo(get())
    }
    single {
        Repository(get())
    }
}
val viewModelModule = module {
    viewModel {
        MyViewModel(get())
    }
    viewModel {
        PdfViewModel(get())
    }
}
