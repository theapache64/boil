package $PACKAGE_NAME.feature.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import $PACKAGE_NAME.utils.livedata.SingleLiveEvent

/**
 * Created by theapache64 : Jul 26 Sun,2020 @ 22:07
 */
open class BaseViewModel : ViewModel() {

    private val _snackBarMsg = SingleLiveEvent<Any>()
    val snackBarMsg: LiveData<Any> = _snackBarMsg

    protected val _toastMsg = MutableLiveData<Any>()
    val toastMsg: LiveData<Any> = _toastMsg
}
