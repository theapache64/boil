package $PACKAGE_NAME.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * To show short toast message
 */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
}

/**
 * To show short toast message with xml string
 */
fun Context.toast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
}


/**
 * To show long toast message
 */
fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
}

/**
 * To show long toast message with xml string
 */
fun Context.longToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
}
