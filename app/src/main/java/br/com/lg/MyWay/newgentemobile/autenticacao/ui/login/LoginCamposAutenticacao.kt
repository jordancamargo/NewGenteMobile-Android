package br.com.lg.MyWay.newgentemobile.autenticacao.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.lg.MyWay.newgentemobile.R
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue200
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue500
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue700
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue900

@Composable
fun LoginCamposAutenticacao() {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var manterConectado by remember { mutableStateOf(false) }

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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_lg_lugar_de_gente),
            contentDescription = "Logo App",
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.White,
                        Blue500,
                        Blue200
                    )
                )
            )
        )
        Spacer(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 50.dp))
        AnimacaoBordaCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 24.dp),
            shape = RoundedCornerShape(8.dp),
            colorContainer = Blue700,
            gradient = Brush.sweepGradient(listOf(Blue500, Blue200))
        ) {
            Column (
                modifier = Modifier.padding(all = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextFieldCustomizado(
                    value = email,
                    onValueChange = {
                                    email = it
                    },
                    containerColor = Blue900,
                    cursorColor = Color.White,
                    hint = stringResource(R.string.email),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    icon = R.drawable.icon_pessoa,
                    iconContentDescription = "Icone Pessoa"
                )
                TextFieldCustomizado(
                    value = senha,
                    onValueChange = {
                                    senha = it
                    },
                    containerColor = Blue900,
                    cursorColor = Color.White,
                    hint = stringResource(R.string.senha),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    icon = R.drawable.icon_senha,
                    iconContentDescription = "Icone Senha",
                    isPassword = true
                )
                Spacer(modifier = Modifier.padding(top = 56.dp))
                Button(
                    onClick = { },
                    modifier = Modifier
                        .width(300.dp)
                        .height(90.dp)
                        .padding(0.dp, 20.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue900,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.entrar),
                        fontSize = 18.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(id = R.string.esqueceu_senha),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.padding(20.dp,0.dp))
                    Text(
                        text = stringResource(id = R.string.primeiro_acesso),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 180.dp, start = 24.dp),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Switch(
                    checked = manterConectado,
                    onCheckedChange = {
                        manterConectado = it
                    },
                    thumbContent = if (manterConectado) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    },
                    colors = SwitchDefaults.colors(
                        checkedBorderColor = Color.Transparent,
                        checkedIconColor = Color.White,
                        checkedThumbColor = Blue900,
                        checkedTrackColor = Blue200,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedThumbColor = Blue900
                    )

                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(id = R.string.manter_conectado),
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

        }
        AnimacaoBordaCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 24.dp),
            shape = RoundedCornerShape(8.dp),
            colorContainer = Blue700,
            gradient = Brush.sweepGradient(listOf(Blue500, Blue200))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp)
            ) {
                CardButton(
                    iconId = R.drawable.icon_marcar_ponto,
                    description = stringResource(id = R.string.marcacao_ponto),
                    onClick = {}
                )
                CardButton(
                    iconId = R.drawable.icon_recibo,
                    description = stringResource(id = R.string.recibo),
                    onClick = {}
                )
                CardButton(
                    iconId = R.drawable.icon_organo,
                    description = stringResource(id = R.string.organograma),
                    onClick = {}
                )
            }
        }
    }
}

@Composable
@Preview
private fun LoginCamposAutenticacaoPreview() {
    LoginCamposAutenticacao()
}
