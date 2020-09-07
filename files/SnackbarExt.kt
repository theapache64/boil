package com.theapache64.nemo.utils.extensions

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * Created by theapache64 : Sep 06 Sun,2020 @ 12:40
 */


/**
 * To show short and simple snackbar with just a message
 */
fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

/**
 *To show Snackbar
 */
fun View.snackBar(@StringRes msgId: Int) {
    snackBar(context.getString(msgId))
}

/**
 * To show long snack bar
 * */
fun View.snackBarLong(@StringRes msgId: Int) {
    snackBarLong(context.getString(msgId))
}

/**
 * To show long snack bar
 */
fun View.snackBarLong(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}
