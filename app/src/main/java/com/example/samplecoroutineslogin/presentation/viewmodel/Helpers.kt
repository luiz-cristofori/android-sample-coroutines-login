package com.example.samplecoroutineslogin.presentation.viewmodel

import android.util.Log
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

/**
 * The function collectEffect will be used to get effect
 * while [SingleLiveEvent] is used to represent one shot events in our architecture.
 *
 * Implementation example:
 * ```
 * // UiEffect
 * sealed class MyUiEffect {
 *   object OpenScreenBAfterAsyncRequest : MyUiEffect()
 * }
 *
 * // ViewModel
 * private val _uiEffect = MutableSingleLiveEvent<MyUiEffect>()
 * val uiEffect = _uiEffect.asSingleLiveEvent()
 *
 * // Compose
 * viewModel.uiEffect.collectEffect {
 *   when (it) {
 *     MyUiEffect.OpenScreenBAfterAsyncRequest -> openScreen()
 *   }
 * }
 * ```
 *
 */
@Composable
fun <T> SingleLiveEvent<T>.collectEffect(block: (T?) -> Unit) {
    val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(key1 = this) {
        val observer = Observer<T> { block(it) }
        this@collectEffect.observe(lifecycle, observer)

        onDispose {
            this@collectEffect.removeObserver(observer)
        }
    }
}

class MutableSingleLiveEvent<T> : SingleLiveEvent<T> {

    constructor() : super()
    constructor(value: T) : super(value)

    @MainThread
    public override fun emit(value: T?) {
        super.emit(value)
    }

    @MainThread
    public override fun setValue(value: T?) {
        super.emit(value)
    }

    public override fun post(value: T?) {
        super.post(value)
    }

    public override fun postValue(value: T?) {
        super.post(value)
    }

    fun asSingleLiveEvent(): SingleLiveEvent<T> = this
}

open class SingleLiveEvent<T> : LiveData<T> {

    constructor() : super()

    constructor(value: T) : super(value)

    private var pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    /**
     * Used to dispatch the value to main-thread.
     * This value arrive first than value sent in [post] function
     * because is dispatched directly to main-thread.
     *
     * @see [MutableLiveData.setValue] documentation
     */
    @MainThread
    protected open fun emit(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    /**
     * Used to dispatch the value in any thread to main-thread.
     *
     * @see [MutableLiveData.postValue] documentation
     */
    protected open fun post(value: T?) {
        pending.set(true)
        super.postValue(value)
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }
}
