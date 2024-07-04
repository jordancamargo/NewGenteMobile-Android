package br.com.lg.MyWay.newgentemobile.configuracoes.seguranca

class ArmazenamentoSeguroNDK {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }

        @JvmStatic
        external fun getIdentityPoolId(): String
    }
}
