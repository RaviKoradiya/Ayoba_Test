package com.ayoba.utils

import android.app.Activity
import android.content.Context
import android.text.InputFilter
import android.text.format.DateFormat
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.ayoba.R
import com.livinglifetechway.k4kotlin.core.addTextWatcher
import com.livinglifetechway.k4kotlin.core.orZero
import java.util.*

fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun Activity.hideActionbar() {
    if (this is AppCompatActivity) supportActionBar?.hide()
}

fun Activity.setText(title: String) {
    if (this is AppCompatActivity) supportActionBar?.apply {
        show()
        this.title = title
    }
}

fun Activity.setSubText(subtitle: String) {
    if (this is AppCompatActivity) supportActionBar?.apply {
        show()
        this.subtitle = subtitle
    }
}

@BindingAdapter("timestamp")
fun TextView.timestamp(timestamp: Long) {

    if (timestamp == 0L) {
        text = ""
        return
    }

    val nowTime = Calendar.getInstance()

    val dateTime = Calendar.getInstance().also { calendar ->
        calendar.timeInMillis = timestamp
    }

    if (dateTime[Calendar.YEAR] != nowTime[Calendar.YEAR]) { // different year
        text = DateFormat.format("MM.dd.yyyy.  ·  HH:mm", dateTime).toString()
        return
    }

    if (dateTime[Calendar.MONTH] != nowTime[Calendar.MONTH]) { // different month
        text = DateFormat.format("MM.dd.  ·  HH:mm", dateTime).toString()
        return
    }

    text = when {
        nowTime[Calendar.DATE] == dateTime[Calendar.DATE] -> { // today
            "${context.getString(R.string.today)}  ·  ${DateFormat.format("HH:mm", dateTime)}"
        }

        nowTime[Calendar.DATE] - dateTime[Calendar.DATE] == 1 -> { // yesterday
            "${context.getString(R.string.yesterday)}  ·  ${DateFormat.format("HH:mm", dateTime)}"
        }

        else -> { // other date this month
            DateFormat.format("MM.dd.  ·  HH:mm", dateTime).toString()
        }
    }
}

@BindingAdapter("counterView")
fun EditText.setCharCounter(@IdRes id: Int) {
    filters.filterIsInstance<InputFilter.LengthFilter?>().firstOrNull()?.let {
        val max = it.max
        addTextWatcher { s, _, _, count ->
            val dif = max - s?.length.orZero()
            rootView.findViewById<TextView>(id).text =
                when (dif) {
                    max -> "$max/$max"
                    else -> "$dif/$max"
                }
            rootView.findViewById<TextView>(id).setTextColorRes(
                when (dif) {
                    0 -> android.R.color.holo_red_dark
                    else -> R.color.black
                }
            )
        }
    }
}

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)