package com.hitss.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Spanned
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpanned
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

/**
 * Converts HTML formatted text to a `Spanned` object that can be used for display.
 *
 * @param text The HTML formatted text to be converted.
 * @return The converted `Spanned` object that can be used for display.
 * If the input `text` is null, returns an empty `Spanned` object.
 */
fun getHtmlText(text: String?): Spanned {
    return HtmlCompat.fromHtml(text ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY).trim().toSpanned()
}

/**
 * Opens a URL in the device's default browser.
 *
 * @param url The URL to be opened.
 * If the URL is not provided or is empty, opens the default URL "https://www.google.com/".
 * If the URL starts with "http", replaces it with "https" before opening.
 */
fun Context.openUrlBrowser(url: String) {

    val openURL = Intent(Intent.ACTION_VIEW)
    if (url.isNotEmpty()) {
        if (url.contains("https://")) {
            openURL.data = Uri.parse(url)

        } else {
            if (url.contains("http")) {
                var newUrl = url.replace("http", "https")
                openURL.data = Uri.parse(newUrl)
            } else {
                openURL.data = Uri.parse("https://$url")
            }
        }

    } else {
        openURL.data = Uri.parse("https://www.google.com/")
    }
    startActivity(openURL)
}

/**
 * Returns the current date in a literal format using Spanish language.
 *
 * The format is "EEEE dd 'de' MMMM yyyy" (e.g. "jueves 25 de febrero 2022").
 *
 * @return The current date in a literal format using Spanish language.
 */
fun getTodayLiteral(): String {
    val sdf = SimpleDateFormat("EEEE dd 'de' MMMM yyyy", Locale("es", "MX"))
    return sdf.format(Date())
}

/**
 * Displays a MaterialDatePicker for selecting a date.
 *
 * The selected date is passed to the provided callback as two formats:
 * - yyyy-MM-dd
 * - EEEE, d 'de' MMMM 'de' yyyy (e.g. "jueves, 25 de febrero de 2022")
 *
 * @param manager The FragmentManager to use to show the date picker.
 * @param callback A callback to be invoked when a date is selected. The selected date is passed as two formats.
 */
fun showDatePicker(
    manager: FragmentManager,
    callback: (date: String, dateLiteral: String) -> Unit
) {
    val now = Calendar.getInstance()
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setSelection(now.timeInMillis)
        .build()

    datePicker.showNow(manager, "date")
    datePicker.addOnPositiveButtonClickListener {
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.timeInMillis = it
        selectedCalendar.add(Calendar.DAY_OF_MONTH, 1)
        val selectedDate = selectedCalendar.time
        val newDate = SimpleDateFormat("yyyy-MM-dd", Locale("es", "MX"))
        val sdf = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "MX"))
        val stringDate = sdf.format(selectedDate)

        callback.invoke(newDate.format(selectedDate), stringDate)
    }
}

/**
 * Hides the keyboard from the current view.
 */
fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}
/**
 * Shows the keyboard from the current view.
 */
fun EditText.showKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}
