package com.tcc.poc.feature.ui.fragments.util

import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun String.formatDate(): String {
    // Parse the input string to LocalDateTime
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault())
    val dateTime = LocalDateTime.parse(this, inputFormatter)

    // Format it to the desired output
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
    return dateTime.format(outputFormatter)
}
fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}