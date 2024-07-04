package br.com.lg.MyWay.newgentemobile.compartilhado.utils

import android.content.Context
import android.net.ConnectivityManager
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao


object NetworkUtils {
    fun isConnected(context: Context): Boolean {
        return try {
            val connMgr =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            wifi!!.isConnected || mobile!!.isConnected || Sessao.ehModoDemonstracao(context)
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }
}

