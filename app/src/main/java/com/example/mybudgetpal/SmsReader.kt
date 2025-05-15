package com.example.mybudgetpal

import android.content.Context
import android.net.Uri
import android.provider.Telephony
import java.text.SimpleDateFormat
import java.util.*

data class Expense(val day: String, val amount: Float)

fun readGPaySms(context: Context): List<Expense> {
    val expenses = mutableListOf<Expense>()
    val uri: Uri = Telephony.Sms.Inbox.CONTENT_URI
    val projection = arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE)

    val cursor = context.contentResolver.query(uri, projection, null, null, null)

    cursor?.use {
        while (it.moveToNext()) {
            val sender = it.getString(0)
            val body = it.getString(1)
            val timestamp = it.getLong(2)

            if (sender.contains("GPAY", ignoreCase = true) && body.contains("debited", true)) {
                val amount = Regex("₹\\s?([0-9,]+\\.?[0-9]*)")
                    .find(body)?.groups?.get(1)?.value?.replace(",", "")?.toFloatOrNull()

                amount?.let {
                    val day = SimpleDateFormat("EEE", Locale.getDefault()).format(Date(timestamp))
                    expenses.add(Expense(day, it))
                }
            }
        }
    }

    return expenses
}
