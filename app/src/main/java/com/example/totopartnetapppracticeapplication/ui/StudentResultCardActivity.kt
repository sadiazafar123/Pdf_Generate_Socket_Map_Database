package com.example.totopartnetapppracticeapplication.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.totopartnetapppracticeapplication.R
import com.example.totopartnetapppracticeapplication.api.EvaluationDetail
import com.example.totopartnetapppracticeapplication.api.Globals
import com.example.totopartnetapppracticeapplication.api.Status
import com.example.totopartnetapppracticeapplication.databinding.ActivityStudentResultCardBinding
import com.example.totopartnetapppracticeapplication.viewmodel.PdfViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class StudentResultCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentResultCardBinding
    private val viewModel: PdfViewModel by viewModel()
    private lateinit var evaluationDetail : ArrayList<EvaluationDetail>
    lateinit var params: HashMap<String, String>



    var PERMISSION_CODE = 101
    var userIdentity = "E94Zo5eRQcRoDh1HvDqSYIeYSBHJWdB9"
    var mobileCode = 444
    var month = 3
    var notificationTypeId = 0
    var studentId = 1
    var year = 2023
    ///pixel resolution of A4 page: 2480 * 3508
  //  var pageWidth = 792
    var pageWidth = 700
    var pageHeight = 1120
   // var x = 250F
    var y0 =200F
    var y1 = 230F
    var y2 = 250F
    var y3 = 360F

    private val layoutResID: Int @LayoutRes get() = R.layout.activity_student_result_card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,layoutResID)
        if (checkpermission()){
            Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
        }
        else{
            requestPermission()
        }
        initViews()
    }
    private fun initViews() {
        if (checkpermission()){
            Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
        }
        else{
            requestPermission()
        }
        binding.idBtnGeneratePdf.setOnClickListener {
            generatePDF()

        }
        ApiCalling()


    }
    private fun checkpermission() :Boolean {
        val writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {

        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE){
            if (grantResults.size>0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()

                }
                else {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    checkpermission()
                }

            }
        }
    }

    private fun generatePDF() {
        val pdfDocument: PdfDocument = PdfDocument()
        val title1: Paint = Paint()
        val title2: Paint = Paint()
        val myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)
        val canvas: Canvas = myPage.canvas
        title1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        title2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        title1.textSize = 14F
        title2.textSize = 11F
        title1.setColor(ContextCompat.getColor(this, R.color.black))
        title2.setColor(ContextCompat.getColor(this, R.color.black))
        canvas.drawText("Student ID", 80F, 100F, title1)
        canvas.drawText("1", 180F, 100F, title1)
        canvas.drawText("Name", 80F, 120F, title1)
        canvas.drawText("Sadia Zafar", 180F, 120F, title1)
        canvas.drawText("Class", 80F, 140F, title1)
        canvas.drawText("7th A", 180F, 140F, title1)
        canvas.drawText("School", 80F, 160F, title1)
        canvas.drawText("Lady Bird School", 180F, 160F, title1)
        for (i in 0 until evaluationDetail.size){
            var totalMarks = 0
            var obtainedMarks= 0.0
            val percentage= 0.0
            canvas.drawText("Evaluation :", 80F, y0, title1)
            canvas.drawText(evaluationDetail[i].EvaluationName, 180F, y0, title1)
            canvas.drawText("Date", 80F, y1, title1)
            canvas.drawText("Subject", 180F,y1 , title1)
            canvas.drawText("Total Marks", 280F, y1, title1)
            canvas.drawText("Obt Marks", 390F, y1, title1)
            canvas.drawText("Percentage", 480F, y1, title1)
            y1+= 200
            y0+= 200
            for (j in 0 until evaluationDetail.get(i).EvaluationDetails.size ){
                canvas.drawText(evaluationDetail.get(i).EvaluationDetails.get(j).EvaluationDateString, 80F, y2, title2)
                canvas.drawText(evaluationDetail.get(i).EvaluationDetails.get(j).Subject, 180F, y2, title2)
                canvas.drawText(evaluationDetail.get(i).EvaluationDetails.get(j).TotalMarks.toString(), 300F, y2, title2)
                canvas.drawText(evaluationDetail.get(i).EvaluationDetails.get(j).ObtainedMarks.toString(), 410F, y2, title2)
                canvas.drawText(evaluationDetail.get(i).EvaluationDetails.get(j).Percentage.toString(), 490F, y2, title2)
                y2+= 20F
               totalMarks = totalMarks.plus((evaluationDetail.get(i).EvaluationDetails.get(j).TotalMarks))
               obtainedMarks= obtainedMarks.plus(evaluationDetail.get(i).EvaluationDetails.get(j).ObtainedMarks)

            }
            y2+= 100
            canvas.drawText("Total", 80F, y3, title1)
            canvas.drawText("$totalMarks", 300F, y3, title1)
            canvas.drawText("$obtainedMarks", 410F, y3, title1)
            canvas.drawText("74.8%", 490F, y3, title1)
            y3+= 200

        }
//        ApiCalling()
//       / / result card result ist row
//         canvas.drawText(evaluationDetail.get(0).EvaluationDetails.get(0).EvaluationDateString, 80F, 250F, title2)
//         canvas.drawText("English", 180F, 250F, title2)
//         canvas.drawText("50", 300F, 250F, title2)
//         canvas.drawText("50.00", 410F, 250F, title2)
//         canvas.drawText("100.00%", 490F, 250F, title2)
//        // result card result second row
//        canvas.drawText("2023-03-27", 80F, 270F, title2)
//        canvas.drawText("Urdu", 180F, 270F, title2)
//        canvas.drawText("50", 300F, 270F, title2)
//        canvas.drawText("50.00", 410F, 270F, title2)
//        canvas.drawText("100.00%", 490F, 270F, title2)
//        // result card third row
//        canvas.drawText("2023-03-27", 80F, 290F, title2)
//        canvas.drawText("Math", 180F, 290F, title2)
//        canvas.drawText("50", 300F, 290F, title2)
//        canvas.drawText("50.00", 410F, 290F, title2)
//        canvas.drawText("100.00%", 490F, 290F, title2)
//        //result card 4th row
//        canvas.drawText("2023-03-27", 80F, 310F, title2)
//        canvas.drawText("Pak Study", 180F, 310F, title2)
//        canvas.drawText("50", 300F, 310F, title2)
//        canvas.drawText("50.00", 410F, 310F, title2)
//        canvas.drawText("100.00%", 490F, 310F, title2)
//        //result card 5th
//        canvas.drawText("2023-03-27", 80F, 330F, title2)
//        canvas.drawText("Islamiyat", 180F, 330F, title2)
//        canvas.drawText("50", 300F, 330F, title2)
//        canvas.drawText("50.00", 410F, 330F, title2)
//        canvas.drawText("100.00%", 490F, 330F, title2)
//        ///total count
//        canvas.drawText("Total", 80F, 360F, title1)
//        canvas.drawText("250", 300F, 360F, title1)
//        canvas.drawText("187.00", 410F, 360F, title1)
//        canvas.drawText("74.8%", 490F, 360F, title1)
//        ///second result card
//        canvas.drawText("Evaluation", 80F, 400F, title1)
//        canvas.drawText("Testing Exam", 180F, 400F, title1)
//        ///top heading
//        canvas.drawText("Date", 80F, 430F, title1)
//        canvas.drawText("Subject", 180F, 430F, title1)
//        canvas.drawText("Total Marks", 280F, 430F, title1)
//        canvas.drawText("Obt Marks", 390F, 430F, title1)
//        canvas.drawText("Percentage", 480F, 430F, title1)
//        /// result
//        // result card result 1
//        canvas.drawText("2023-03-27", 80F, 450F, title2)
//        canvas.drawText(" English", 180F, 450F, title2)
//        canvas.drawText("100", 300F, 450F, title2)
//        canvas.drawText("95.00", 410F, 450F, title2)
//        canvas.drawText("95.00%", 490F, 450F, title2)
//        //2 line
//        canvas.drawText("2023-03-27", 80F, 470F, title2)
//        canvas.drawText(" English", 180F, 470F, title2)
//        canvas.drawText("100", 300F, 470F, title2)
//        canvas.drawText("95.00", 410F, 470F, title2)
//        canvas.drawText("95.00%", 490F, 470F, title2)
//        // 3 line
//        canvas.drawText("2023-03-27", 80F, 490F, title2)
//        canvas.drawText(" English", 180F, 490F, title2)
//        canvas.drawText("100", 300F, 490F, title2)
//        canvas.drawText("95.00", 410F, 490F, title2)
//        canvas.drawText("95.00%", 490F, 490F, title2)
//        //4 line
//        canvas.drawText("2023-03-27", 80F, 510F, title2)
//        canvas.drawText(" English", 180F, 510F, title2)
//        canvas.drawText("100", 300F, 510F, title2)
//        canvas.drawText("95.00", 410F, 510F, title2)
//        canvas.drawText("95.00%", 490F, 510F, title2)
//        //5 line
//        canvas.drawText("2023-03-27", 80F, 530F, title2)
//        canvas.drawText(" English", 180F, 530F, title2)
//        canvas.drawText("100", 300F, 530F, title2)
//        canvas.drawText("95.00", 410F, 530F, title2)
//        canvas.drawText("95.00%", 490F, 530F, title2)
//        //total result
//        canvas.drawText("Total", 80F, 560F, title1)
//        canvas.drawText("250", 300F, 560F, title1)
//        canvas.drawText("187.00", 410F, 560F, title1)
//        canvas.drawText("74.8%", 490F, 560F, title1)
//
        pdfDocument.finishPage(myPage)
        val file: File = File(Environment.getExternalStorageDirectory(), "ResultCard.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(applicationContext, "PDF file generated..", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }

        pdfDocument.close()
    }

    private fun ApiCalling() {
        params = HashMap()
        params["UserIdentity"] = userIdentity
        params["MobileCode"] = mobileCode.toString()
        params["Month"] = month.toString()
        params["NotificationTypeId"] = notificationTypeId.toString()
        params["StudentId"] = studentId.toString()
        params["Year"] = year.toString()
        viewModel.getStudentRecord(params).observe(this@StudentResultCardActivity){ apiResponse ->
            when(apiResponse.status){
                Status.LOADING ->{
                    Globals.showProgressDialog(this)
                }
                Status.SUCCESS ->{
                    Globals.hideProgressDialog()
                    if (apiResponse.data!= null){
                        evaluationDetail= ArrayList()
                        if (apiResponse.data.code() == 200){
                            val topResponse= apiResponse.data.body()
                            topResponse?.let {
                                evaluationDetail.addAll(topResponse.Evaluations)
                            }
                            Log.d("response"," student record is :$evaluationDetail[0]")
                        }
                        else{
                            Toast.makeText(this@StudentResultCardActivity, "${apiResponse.data.errorBody()}", Toast.LENGTH_SHORT).show()
                            Log.d("response","else branch: ${apiResponse.data.errorBody()}")

                            //showToast(AppConstants.setApiErrorResponse(serverResponse.data.errorBody(), serverResponse.data.code()))

                        }
                    }
                }
                Status.ERROR ->{
                    Globals.hideProgressDialog()
                    Log.d("response",": errror branch ${apiResponse.message}")
                    Toast.makeText(this@StudentResultCardActivity, "${apiResponse.message}", Toast.LENGTH_SHORT).show()

                }
            }
        }

        }

}
