package br.com.lg.MyWay.newgentemobile

import android.os.Bundle
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
}