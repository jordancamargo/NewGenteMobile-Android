package br.com.lg.MyWay.newgentemobile.compartilhado.network.models

import br.com.lg.MyWay.newgentemobile.compartilhado.network.Conexao
import br.com.lg.MyWay.newgentemobile.compartilhado.network.RequisicaoListener
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.TelaUtils


abstract class Servico : RequisicaoListener {
    protected var conexao: Conexao

    init {
        conexao = Conexao(this)
    }

    protected fun execute(requisicao: Requisicao?) {
        conexao.execute(requisicao)
    }

    fun getConexao(): Conexao {
        return conexao
    }

    protected fun logMensagemDeErro(msg: String?, excecao: Throwable?) {}
    fun onDownloadCompleto(tipo: EnumTipoRequisicao?, file: DownloadedObject?) {}
    fun onDownloadCompleto(requisicao: Requisicao, file: DownloadedObject?) {
        onDownloadCompleto(requisicao.getTipoRequisicao(), file)
    }

    fun onFalhaConexaoRetry(tentativa: Int, stop: Boolean) {
        if (stop) {
            TelaUtils.fecharSnackBar()
        } else {
            TelaUtils.exibirSnackBar(tentativa)
        }
    }
}

