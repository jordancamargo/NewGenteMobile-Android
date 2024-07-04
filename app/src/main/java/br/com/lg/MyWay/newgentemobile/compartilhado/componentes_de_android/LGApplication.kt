package br.com.lg.MyWay.newgentemobile.compartilhado.componentes_de_android

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner


class LGApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleListener())
        instance = this
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    companion object {
        var instance: LGApplication? = null
            private set
        val appBaseContext: Context
            get() = instance!!.baseContext
    }
}

