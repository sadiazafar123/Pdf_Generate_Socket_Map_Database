package com.example.totopartnetapppracticeapplication

import com.example.totopartnetapppracticeapplication.localdb.LocalRepo
import com.example.totopartnetapppracticeapplication.viewmodel.MyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single{
      LocalRepo(get())
    }
}
val viewModel= module {
    viewModel {
        MyViewModel(get())
    }
}
