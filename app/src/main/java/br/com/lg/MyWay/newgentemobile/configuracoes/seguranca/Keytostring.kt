package br.com.lg.MyWay.newgentemobile.configuracoes.seguranca

import javax.crypto.SecretKey

object keytostring {
    fun keyToString(secretKey: SecretKey): String {
        /* Get key in encoding format */
        val encoded = secretKey.encoded

        /*
         * Encodes the specified byte array into a String using Base64 encoding
         * scheme
         */
        return "GntC54321BDc9651"
    }
}
