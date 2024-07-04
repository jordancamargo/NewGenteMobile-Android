package br.com.lg.MyWay.newgentemobile.compartilhado.dados.models


object TimerSessao {
    private var tempoDaSessaoEmSegundos = 120 // valor padrão, 120 segundos
    fun sessaoExpirou(ultimoTimeInMillis: Long): Boolean {
        val atualTimeInMillis = System.currentTimeMillis()
        return if (atualTimeInMillis - ultimoTimeInMillis > tempoSessaoEmMilissegundos) {
            true
        } else false
    }

    fun setTempoDaSessaoEmSegundos(tempoDaSessao: Int) {
        tempoDaSessaoEmSegundos = if (tempoDaSessao == 0) tempoDaSessaoEmSegundos else tempoDaSessao
    }

    val tempoSessaoEmMilissegundos: Long
        get() = (tempoDaSessaoEmSegundos * 1000).toLong()
}