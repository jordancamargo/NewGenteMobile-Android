package br.com.lg.MyWay.newgentemobile.compartilhado.componentes_de_android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.TimerSessao.tempoSessaoEmMilissegundos
import java.util.Timer
import java.util.TimerTask


class LifecycleListener : LifecycleObserver {
    private var timer: Timer? = null
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {}
        }
        timer!!.schedule(task, tempoSessaoEmMilissegundos)
    }
}

