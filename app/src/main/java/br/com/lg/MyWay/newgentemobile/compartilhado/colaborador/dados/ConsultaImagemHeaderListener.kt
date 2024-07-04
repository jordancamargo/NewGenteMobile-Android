package br.com.lg.MyWay.newgentemobile.compartilhado.colaborador.dados

import android.graphics.Bitmap

interface ConsultaImagemHeaderListener {
    fun onSucessoConsultarImagem(imagem: Bitmap?)
    fun onFalhaConsultarImagem(msgErro: String?)
}
