package com.example.totopartnetapppracticeapplication.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.totopartnetapppracticeapplication.R
import com.example.totopartnetapppracticeapplication.databinding.ActivityPdfGenerateBinding
import java.io.File
import java.io.FileOutputStream


class PdfGenerateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfGenerateBinding
    // creating a bitmap variable
    // for storing our images
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap
    var pageHeight = 1120
   // var pageHeight = 1600
    //var pageWidth = 792
    var pageWidth = 792
    var PERMISSION_CODE = 101
    private val layoutResID: Int @LayoutRes get() = R.layout.activity_pdf_generate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResID)
        bmp = BitmapFactory.decodeResource(resources, R.drawable.pdfimg)
        scaledbmp = Bitmap.createScaledBitmap(bmp, 700, 1100, false)
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

    }

    private fun generatePDF() {
        val pdfDocument: PdfDocument = PdfDocument()
        val title: Paint = Paint()
        val myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)
        val canvas: Canvas = myPage.canvas
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        title.textSize = 50F
        title.setColor(ContextCompat.getColor(this, R.color.green))
        canvas.drawText("Pet Breed", 50f, 100F, title)
        canvas.drawText("Pet Name", 50f, 250F, title)
        canvas.drawText("Pet Weight(Lbs)", 50f, 400F, title)
        canvas.drawText("Pet Date Of Birth (DD-MM-YYYY)", 50f, 550F, title)
        canvas.drawText("Favorite Food(Brand)", 50f, 700F, title)
        canvas.drawText("Favorite Toys",50f,850F,title)
        //canvas.drawText("Favorite Toys", 50f, 600F, title)

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.setColor(ContextCompat.getColor(this, R.color.black))
        title.textSize = 30F
      //  title.textAlign = Paint.Align.CENTER
       canvas.drawText("Booli", 50f, 160F, title)
       canvas.drawText("Tommy", 50f, 310F, title)
       canvas.drawText("30", 50f, 460F, title)
       canvas.drawText("27-01-2023", 50f, 610F, title)
       canvas.drawText("BBQ", 50f, 760F, title)
       canvas.drawText("Football",50f,910F,title)
     //  canvas.drawText("Football", 50f, 640F, title)
        pdfDocument.finishPage(myPage)

        ///page 2//////////////////////////
        val title2: Paint = Paint()
        val myPageInfo2: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 2).create()
        val myPage2: PdfDocument.Page = pdfDocument.startPage(myPageInfo2)
        val canvas2: Canvas = myPage2.canvas
        title2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        title2.textSize = 50F
        title2.setColor(ContextCompat.getColor(this, R.color.green))
        canvas2.drawText("Pet Guardian/Beneficiary", 50f, 100F, title2)
        canvas2.drawText("Beneficiary Contact", 50f, 250F, title2)
        canvas2.drawText("Primary Veterinarian)", 50f, 400F, title2)
        canvas2.drawText("Pet Mannerism", 50f, 550F, title2)
        canvas2.drawText("Daily Routine", 50f, 700F, title2)
        canvas2.drawText("Medical/Injury History", 50f, 850F, title2)
        title2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title2.setColor(ContextCompat.getColor(this, R.color.black))
        title2.textSize = 30F
        //  title2.textAlign = Paint.Align.CENTER
        canvas2.drawText("Pet Guardian", 50f, 160F, title2)
        canvas2.drawText("0305-9842861", 50f, 310F, title2)
        canvas2.drawText("Jenny", 50f, 460F, title2)
        canvas2.drawText("Dance", 50f, 610F, title2)
        canvas2.drawText("Goes for a walk to nearby park", 50f, 760F, title2)
        canvas2.drawText("None", 50f, 910f, title2)
        pdfDocument.finishPage(myPage2)
        ///page 3
        val paint: Paint = Paint()
        val myPageInfo3: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 3).create()
        val myPage3: PdfDocument.Page = pdfDocument.startPage(myPageInfo3)
        val canvas3: Canvas = myPage3.canvas
        canvas3.drawBitmap(scaledbmp, 56F, 40F, paint)
        pdfDocument.finishPage(myPage3)

        val file: File = File(Environment.getExternalStorageDirectory(), "my.pdf")
       // val file: File = File(DIRECTORY_DOWNLOADS, "GFG.pdf")
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

//            // on below line we are displaying a toast message as PDF file generated..
            Toast.makeText(applicationContext, "PDF file generated..", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception) {
            // below line is used
            // to handle error
            e.printStackTrace()
            // on below line we are displaying a toast message as fail to generate PDF
            Toast.makeText(applicationContext, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }

        pdfDocument.close()



    }


    private fun checkpermission() :Boolean {
        val writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )
        val readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission() {

        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE
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

}