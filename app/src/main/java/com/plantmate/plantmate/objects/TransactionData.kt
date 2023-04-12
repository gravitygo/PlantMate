package com.plantmate.plantmate.objects

import com.google.firebase.Timestamp

class TransactionData(
    val date: Timestamp,
    val cost: Number,
    val plantID: String,
    val transactionType: String) {
}