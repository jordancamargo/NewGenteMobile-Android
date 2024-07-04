package br.com.lg.MyWay.newgentemobile.configuracoes.seguranca

class KeystoreManagerException(message: String?) : Exception(message) {
    companion object {
        const val EXCEPTION_EMPTY_TEXT = "Error attempting to encrypt an empty String"
    }
}