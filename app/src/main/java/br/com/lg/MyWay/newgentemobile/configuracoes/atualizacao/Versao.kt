package br.com.lg.MyWay.newgentemobile.configuracoes.atualizacao

import android.content.Context
import android.content.pm.PackageManager
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.StringAndTextUtils

object Versao {

    const val ULTIMA = 202104061
    const val PRODUTO_NO_WAY = 30000
    const val NOVO_FLUXO = PRODUTO_NO_WAY
    const val RELATORIOS = PRODUTO_NO_WAY
    const val LINKS = PRODUTO_NO_WAY
    const val ULTIMA_COM_WEBSITE_PROPRIO = 2
    const val INTEGRACAO_WORKFLOW = 2
    const val DESCONHECIDA = -1

    fun isVersaoAtualizada(context: Context, versaoPlayStore: String): Boolean {
        return try {
            val versaoStore = obterVersaoNumerica(versaoPlayStore)
            var versao = ""
            try {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                versao = pInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            if (StringAndTextUtils.isNullOrEmpty(versao)) {
                return true
            }
            val versaoNumerica = obterVersaoNumerica(versao)

            for (i in versaoNumerica.indices) {
                when {
                    versaoNumerica[i] == versaoStore[i] -> continue
                    versaoNumerica[i] < versaoStore[i] -> return false
                    else -> return true
                }
            }
            true
        } catch (e: Exception) {
            true
        }
    }

    private fun obterVersaoNumerica(versao: String): IntArray {
        val versaoTexto = versao.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return IntArray(versaoTexto.size) { i -> versaoTexto[i].toInt() }
    }
}
