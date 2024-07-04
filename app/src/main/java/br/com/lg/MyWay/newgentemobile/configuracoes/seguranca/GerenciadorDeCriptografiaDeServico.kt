package br.com.lg.MyWay.newgentemobile.configuracoes.seguranca

import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.isCriptografarDados
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.CriptografiaUtil.criptografarAES
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair

object GerenciadorDeCriptografiaDeServico {
    const val CRIPTOGRAFIA_WORKFLOW = 2
    const val CRIPTOGRAFIA_GERALV3 = 3
    const val CRIPTOGRAFIA_GERALV5 = 5
    private const val ID_SESSAO = "idSessao"

    fun criptografarDadosSeEstiverHabilitado(params: MutableList<NameValuePair>): List<NameValuePair> {
        if (isCriptografarDados) {
            val len = params.size
            var i = 0
            while (i < len) {
                if (!params[i].name.equals(ID_SESSAO, ignoreCase = true)) {
                    val nameValue: NameValuePair = params.removeAt(i)
                    val nameValueCriptogr: NameValuePair = BasicNameValuePair(
                        nameValue.name,
                        criptografarAES(nameValue.value)
                    )
                    params.add(i, nameValueCriptogr)
                }
                i++
            }
        }
        return params
    }
}
