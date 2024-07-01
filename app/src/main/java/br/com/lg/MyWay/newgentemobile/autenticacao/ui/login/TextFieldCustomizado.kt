package br.com.lg.MyWay.newgentemobile.autenticacao.ui.login

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.com.lg.MyWay.newgentemobile.R
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldCustomizado(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    icon: Int = R.drawable.icon_pessoa,
    iconContentDescription: String? = null,
    isPassword: Boolean = false
) {
    TextField(
        value,
        onValueChange,
        modifier,
        label = {
            Text(text = hint, color = Color.White)
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Blue900,
            cursorColor = Color.White
        ),
        maxLines = 1,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 18.sp
        ),
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        leadingIcon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = iconContentDescription,
                tint = Color.White
            )
        }
    )
}

@Composable
@Preview
private fun TextFieldCustomizadoPreview() {
    var email by remember {
        mutableStateOf("")
    }

    TextFieldCustomizado(
        value = email,
        onValueChange = {
            email = it
        },
        hint = "Email",
        icon = R.drawable.icon_pessoa,
        iconContentDescription = "Icone Pessoa"
    )
}
