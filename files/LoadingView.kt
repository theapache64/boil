package $PACKAGE_NAME.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import $PACKAGE_NAME.databinding.LoadingViewBinding

@Suppress("MemberVisibilityCanBePrivate")
class LoadingView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var hasRetryCallback: Boolean = false
    private val binding: LoadingViewBinding

    init {
        val inflater = LayoutInflater.from(getContext())
        this.binding = LoadingViewBinding.inflate(inflater, this, true)
        hideLoading()
    }

    /**
     * Setting retry callback
     */
    fun setRetryCallback(retryCallback: () -> Unit) {
        this.hasRetryCallback = true
        binding.bRetry.setOnClickListener {
            retryCallback()
        }
    }

    /**
     * To show progress bar with given message
     */
    fun showLoading(message: String) {
        visibility = View.VISIBLE

        // View visibility
        binding.clpb.visibility = View.VISIBLE
        binding.tvMessage.visibility = View.VISIBLE
        binding.ivError.visibility = View.GONE
        binding.bRetry.visibility = View.GONE

        // Data change
        binding.tvMessage.text = message
    }

    fun showLoading(@StringRes message: Int) {
        showLoading(context.getString(message))
    }

    fun hideLoading() {
        visibility = View.GONE
    }

    fun showError(message: String) {
        visibility = View.VISIBLE

        // View visibility
        binding.tvMessage.visibility = View.VISIBLE
        binding.ivError.visibility = View.VISIBLE
        binding.clpb.visibility = View.GONE
        binding.bRetry.visibility = if (hasRetryCallback) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.tvMessage.text = message
    }

    fun setTextColor(@ColorRes color: Int) {
        binding.tvMessage.setTextColor(ContextCompat.getColor(context, color))
    }

    fun setErrorRes(@DrawableRes drawable: Int) {
        binding.ivError.setImageResource(drawable)
    }
}
