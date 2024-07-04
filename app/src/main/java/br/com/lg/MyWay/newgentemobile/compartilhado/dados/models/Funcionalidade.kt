package br.com.lg.MyWay.newgentemobile.compartilhado.dados.models

import java.io.Serializable


class Funcionalidade(var id: String, var descricao: String) : Serializable {
    private var visivelParaRescindido = false

    fun ehVisivelParaRescindido(): Boolean {
        return visivelParaRescindido
    }

    fun setVisivelParaRescindido(vs: Boolean) {
        visivelParaRescindido = vs
    }
}

