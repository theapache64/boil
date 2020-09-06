package $PACKAGE_NAME.ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import $PACKAGE_NAME.R


class CustomSwipeRefreshLayout(context: Context, attrs: AttributeSet?) :
    SwipeRefreshLayout(context, attrs) {

    init {

        // Getting colors from implementing module
        val colorPrimary = resolveColor(context, attr = R.attr.colorPrimary)
        val colorPrimaryDark = resolveColor(context, attr = R.attr.colorPrimaryDark)

        setColorSchemeColors(
            colorPrimary,
            colorPrimaryDark,
            colorPrimary
        )

        // setting empty refresh listener to cancel the refresh at the time of refreshing.
        // the above given statement might seem weird, but due to the presense of LoadingView, I 've decided to multiple
        // loading progress in one screen.
        setOnRefreshListener { }
    }

    override fun setOnRefreshListener(listener: OnRefreshListener?) {
        super.setOnRefreshListener {
            listener?.onRefresh()
            isRefreshing = false
        }
    }

    companion object {
        /**
         * To get color from implementing module
         */
        @ColorInt
        private fun resolveColor(
            context: Context,
            @ColorRes res: Int? = null,
            @AttrRes attr: Int? = null,
            fallback: (() -> Int)? = null
        ): Int {
            if (attr != null) {
                val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
                try {
                    val result = a.getColor(0, 0)
                    if (result == 0 && fallback != null) {
                        return fallback()
                    }
                    return result
                } finally {
                    a.recycle()
                }
            }
            return ContextCompat.getColor(context, res ?: 0)
        }
    }
}


