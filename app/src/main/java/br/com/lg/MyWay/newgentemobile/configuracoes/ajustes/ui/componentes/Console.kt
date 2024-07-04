package br.com.lg.MyWay.newgentemobile.configuracoes.ajustes.ui.componentes

import android.text.TextUtils
//import br.com.lg.MyWay.newgentemobile.BuildConfig


object Console {
    private var log1: ArrayList<String?>? = null
    private var log2: ArrayList<String?>? = null
    private const val limiteLog = 30
    fun setLog(objeto: String) {
        try {
            log = objeto.toString()
        } catch (ex: Exception) {
        }
    }

    var log: String?
        get() {
            var result = ""
            if (log1 != null) {
                result += TextUtils.join("\r\n", log1!!)
            }
            if (log2 != null) {
                result += TextUtils.join("\r\n", log2!!)
            }
            return result
        }
        set(texto) {
            try {
                if (log1 == null) {
                    log1 = ArrayList()
                }
                if (log2 == null) {
                    log2 = ArrayList()
                }
//                if (BuildConfig.DEBUG) {
//                    log1!!.add(texto)
//                    log1!!.add("-----------------------------------")
//                } else {
//                    if (log1!!.toTypedArray().size >= limiteLog) {
//                        log2!!.add(texto)
//                        log2!!.add("-----------------------------------")
//                        log1 = ArrayList()
//                    } else {
//                        log1!!.add(texto)
//                        log1!!.add("-----------------------------------")
//                        log2 = ArrayList()
//                    }
//                }
            } catch (ex: Exception) {
            }
        }
}

