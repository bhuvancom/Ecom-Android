package com.tcs.ecom.utility

import android.content.Context
import androidx.appcompat.app.AlertDialog

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    3:42 PM
Project Ecom
 */
object Util {

//    suspend fun <T> doSafeCall(work: suspend ((T) -> Unit), dispatcher: CoroutineDispatcher) =
//        CoroutineScope(dispatcher).launch {
//            try {
//                work(T)
//            } catch (error: Exception) {
//
//            }
//        }


    fun showAlert(
        context: Context,
        onYes: () -> Unit,
        onNo: () -> Unit,
        title: String,
        message: String
    ) {
        AlertDialog.Builder(context)
            .setPositiveButton("Yes") { _, _ ->
                onYes()
            }
            .setNegativeButton("No") { w, _ ->
                onNo()
                w.dismiss()
            }
            .setTitle(title)
            .setMessage(message)
            .show()
    }
}