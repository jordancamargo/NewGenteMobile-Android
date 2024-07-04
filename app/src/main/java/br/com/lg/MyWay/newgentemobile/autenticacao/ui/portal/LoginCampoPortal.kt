package br.com.lg.MyWay.newgentemobile.autenticacao.ui.portal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.lg.MyWay.newgentemobile.R
import br.com.lg.MyWay.newgentemobile.autenticacao.ui.login.AnimacaoBordaCard
import br.com.lg.MyWay.newgentemobile.autenticacao.ui.login.TextFieldCustomizado
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue200
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue500
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue900

@Composable
fun LoginCampoPortal() {
    var portal by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.sweepGradient(
                    listOf(
                        Color.Black,
                        Blue900,
                        Color.Black
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.lg_suite_gente_horizontal),
            contentDescription = "Logo Suite Gente"
        )
        Spacer(modifier = Modifier.padding(0.dp,180.dp))
        AnimacaoBordaCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 24.dp),
            shape = RoundedCornerShape(8.dp),
            colorContainer = Color.White,
            gradient = Brush.sweepGradient(listOf(Blue500, Blue200))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextFieldCustomizado(
                    value = portal,
                    onValueChange = {
                        portal = it
                    },
                    hint = stringResource(id = R.string.portal),
                    containerColor = Color.Transparent,
                    cursorColor = Color.Black,
                    icon = null
                )
            }
        }
        Spacer(modifier = Modifier.padding(0.dp,10.dp))
        Button(
            onClick = {
                      conectarPortal()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue900,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.continuar),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun conectarPortal() {

}


@Composable
@Preview
fun LoginCampoPortalPreview() {
    LoginCampoPortal()
}