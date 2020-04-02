package com.rvtechnologies.grigorahq

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.crashlytics.android.Crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.utils.ApiConstants
import io.fabric.sdk.android.Fabric
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class MyApplication : Application() {

    var listeners = ArrayList<EventBroadcaster>()

    override fun onCreate() {
        super.onCreate()
        instance = this
        enableDebugMode()

        connectSocket()
    }

    companion object {
        var mSocket: Socket? = null
        @get:Synchronized
        var instance: MyApplication? = null
            private set
    }

    fun getContext(): Context {
        return this
    }

    fun updateData(eventType: Int, `object`: kotlin.Any?) {
        Handler(Looper.getMainLooper()).post {
            for (listener in listeners) {
                try {
                    listener.broadcast(eventType, `object`!!)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun registerListener(listener: EventBroadcaster) {
        if (!listeners.contains(listener))
            listeners.add(listener)
    }

    fun deRegisterListener(listener: EventBroadcaster) {
        if (listeners.contains(listener))
            listeners.removeAt(listeners.indexOf(listener))
    }

    fun connectSocket() {
        try {
            mSocket = IO.socket(ApiConstants.SOCKET_URL)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }
    fun enableDebugMode() {
        // [STAR T crash_enable_debug_mode]
        val fabric = Fabric.Builder(this)
            .kits(Crashlytics())
            .debuggable(true)  // Enables Crashlytics debugger
            .build()
        Fabric.with(fabric)
        // [END crash_enable_debug_mode]
    }
}