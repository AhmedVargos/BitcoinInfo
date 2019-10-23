package com.example.bitcoininfoapp.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.example.bitcoininfoapp.R

fun Activity.hideSoftKeyboard() {
    val focusedView = currentFocus
    focusedView?.let { view ->
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


/**
 * Extension method to provide show keyboard for View.
 */
fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

/**
 * Extension method to provide show keyboard for View.
 */
fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}


fun ImageView.loadImageFromUrl(
    url: String?,
    placeHolder: Int? = null
) {
    val requestOptions = RequestOptions()
        .placeholder(placeHolder ?: R.color.colorAccent)

    url?.let {
        GlideApp.with(context)
            .asDrawable()
            .apply(requestOptions)
            .load(url)
            .fitCenter()
            .into(this)
    }

}