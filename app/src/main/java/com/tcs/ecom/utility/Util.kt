package com.tcs.ecom.utility

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.tcs.ecom.models.ApiError
import com.tcs.ecom.models.CartResponse
import com.tcs.ecom.models.OrderForm

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
    private const val TAG = "Util"
    fun getApiError(string: String): ApiError {
        val gson = Gson()
        return try {
            gson.fromJson(string, ApiError::class.java)
        } catch (e: Exception) {
            Log.d(TAG, "getApiError: $e")
            e.printStackTrace()

            ApiError("${e.message}", 500)

        }
    }

    fun addItemToCart(product: OrderForm, cartResponse: CartResponse): CartResponse {
        val find: OrderForm? = cartResponse.cartItems.find {
            it.product.id == product.product.id
        }

        if (find != null) {
            find.quantity++

        } else {
            cartResponse.cartItems.add(product)
        }
        val totalPrice: Double = cartResponse.cartItems.stream().mapToDouble {
            it.quantity * it.product.price
        }.sum()

        return cartResponse.copy(
            cartItems = cartResponse.cartItems,
            id = cartResponse.id,
            numberOfProducts = cartResponse.cartItems.size.toLong(),
            totalCartPrice = totalPrice
        )
    }

    fun removeProductFromCart(product: OrderForm, cartResponse: CartResponse): CartResponse {
        cartResponse.cartItems.remove(product)
        return cartResponse.copy(
            cartItems = cartResponse.cartItems,
            id = cartResponse.id,
            numberOfProducts = cartResponse.cartItems.size.toLong(),
            totalCartPrice = cartResponse.totalCartPrice - product.totalPrice,
        )
    }

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