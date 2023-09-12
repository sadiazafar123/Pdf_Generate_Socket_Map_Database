package com.example.totopartnetapppracticeapplication.api

import retrofit2.Response

class Repository(private val apiInterface: ApiInterface) {
     suspend fun getStudentEvaluationRecord(params: HashMap<String, String>): Response<StudentEvaluationResponse> {
           return apiInterface.getStudentEvaluationRecord(params)


      }

}