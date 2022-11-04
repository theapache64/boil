package $PACKAGE_NAME.util

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember

class Ref(var value: Int)

/**
 * Note the inline function below which ensures that this function is essentially
 * copied at the call site to ensure that its logging only recompositions from the
 * original call site.
 * Author: @vinaygaba
 */
@Composable
@Suppress("NOTHING_TO_INLINE")
inline fun LogCompositions(tag: String, msg: String = "") {

    if (BuildConfig.DEBUG) {
        val ref = remember { Ref(0) }
        SideEffect { ref.value++ }
        Log.d(tag, "Compositions: ${msg} ${ref.value}")
    }
}
