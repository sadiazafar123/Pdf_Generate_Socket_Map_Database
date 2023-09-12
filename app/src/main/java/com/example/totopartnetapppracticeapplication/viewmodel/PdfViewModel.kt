package com.example.totopartnetapppracticeapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.totopartnetapppracticeapplication.api.NetworkStates
import com.example.totopartnetapppracticeapplication.api.Repository
import com.example.totopartnetapppracticeapplication.api.StudentEvaluationResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class PdfViewModel(private val repository: Repository): ViewModel() {
    fun getStudentRecord(params: HashMap<String, String>):LiveData<NetworkStates<Response<StudentEvaluationResponse>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data= repository.getStudentEvaluationRecord(params)))

            }
            catch (e: Exception){
                emit(NetworkStates.error(data = null , message = e.message?: "something went wrong"))
            }

        }


    }
}