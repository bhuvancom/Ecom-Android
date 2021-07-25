package com.tcs.ecom.utility

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import com.google.gson.Gson
import com.tcs.ecom.models.ApiError
import com.tcs.ecom.models.CartResponse
import com.tcs.ecom.models.OrderForm
import com.tcs.ecom.models.Users
import retrofit2.Response

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    3:42 PM
Project Ecom
 */
object Util {

    /**
     * @param work Work which returns reponse
     * @param K type or response
     */
    suspend fun <K> doSafeCall(
        work: suspend () -> Response<K>,
    ): ApiResultState<K> {
        return try {
            val response = work()
            return if (response.isSuccessful && response.body() != null) {
                ApiResultState.SUCCESS(response.body()!!)
            } else {
                response.errorBody()?.apply {
                    val error = this.string()
                    val apiError = getApiError(error)
                    return ApiResultState.ERROR(apiError)
                }
                ApiResultState.ERROR(ApiError("Error occurred in application", 500))
            }
        } catch (error: Exception) {
            ApiResultState.ERROR(ApiError("${error.message}", 500))
        }

    }

    fun addUserInSharedPref(context: Context, users: Users) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.MY_SHARED_PREF, Context.MODE_PRIVATE)
        val gson = Gson()
        val user = gson.toJson(users).toString()
        sharedPreferences.edit {
            putString(Constants.USER_DETAIL, user)
            commit()
        }
    }

    fun removeUserFromSharedPref(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.MY_SHARED_PREF, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            this.remove(Constants.USER_DETAIL)
            commit()
        }
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

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
            .setPositiveButton("Proceed") { w, _ ->
                onYes()
                w.dismiss()

            }
            .setNegativeButton("Cancel") { w, _ ->
                onNo()
                w.dismiss()
            }
            .setCancelable(false)
            .setTitle(title)
            .setMessage(message)
            .show()
    }
}
