package com.example.myapplicationAppsFlyerDemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.appsflyer.deeplink.DeepLink
import com.google.gson.Gson

class DeepLinkViewModel(application: Application) : AndroidViewModel(application) {

    private val _openDeepLink = MutableLiveData<EventWrapper<Pair<String, String>>>()
    val openDeepLink: LiveData<EventWrapper<Pair<String, String>>>
        get() = _openDeepLink

    fun handleAppsFlyerDeepLink(deeplinkObjString: String?) {
        val deepLinkRoute: String
        val deeplinkObject = Gson().fromJson(deeplinkObjString, DeepLink::class.java)
        try {
            deepLinkRoute = deeplinkObject.deepLinkValue.toString()
            _openDeepLink.value = EventWrapper(Pair(deepLinkRoute, ""))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

open class EventWrapper<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    /**
     * Returns the content and prevents its use again.
     */
    fun get(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peek(): T = content
}

class EventObserver<T>(
    private val onEventUnhandledContent: (T) -> Unit
) : Observer<EventWrapper<T>> {
    override fun onChanged(event: EventWrapper<T>?) {
        event?.get()?.let(onEventUnhandledContent)
    }
}