package br.com.lg.MyWay.newgentemobile.compartilhado.componentes_de_android.activities

import android.R
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.lg.MyWay.newgentemobile.autenticacao.ui.login.LoginCamposAutenticacao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginCamposAutenticacao()
        }
    }
    fun obterViewParaSnackbar(): View? {
        return window.decorView.findViewById(R.id.content)
    }
}