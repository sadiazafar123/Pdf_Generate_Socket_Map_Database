package com.example.totopartnetapppracticeapplication.api

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.totopartnetapppracticeapplication.R

object Globals {
    private var progressDialog: Dialog? = null
    fun showProgressDialog(context: Context){
         progressDialog = Dialog(context)
        progressDialog?.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        progressDialog?.setContentView(R.layout.loader)
        progressDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

}
