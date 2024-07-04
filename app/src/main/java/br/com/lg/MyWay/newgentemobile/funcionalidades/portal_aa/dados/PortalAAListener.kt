package br.com.lg.MyWay.newgentemobile.funcionalidades.portal_aa.dados

interface PortalAAListener {
    fun onSucessoRecuperacaoSessao()
    fun onFalhaRecuperacaoSessao(mensagem: String?)
    fun onFalhaConexao()
    fun onMensagemRetornada(mensagem: String?)
}
