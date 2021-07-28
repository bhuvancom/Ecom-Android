package com.tcs.ecom.models

/**
@author Bhuvaneshvar
Date    7/27/2021
Time    11:29 PM
Project Ecom
 */
data class Payment(val customer: String, val ephemeralKey: String, val paymentIntent: String)
