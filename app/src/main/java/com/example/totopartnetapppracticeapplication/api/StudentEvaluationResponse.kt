package com.example.totopartnetapppracticeapplication.api

data class StudentEvaluationResponse(
    val Evaluations: List<EvaluationDetail>
)
data class EvaluationDetail(
    val EvaluationName: String,
    val EvaluationDetails: List<StudentRecord>
)
data class StudentRecord (
    val Subject: String,
    val EvaluationDate: String,
    val EvaluationDateString: String,
    val TotalMarks: Int,
    val ObtainedMarks: Float,
    val Percentage: Float
)
