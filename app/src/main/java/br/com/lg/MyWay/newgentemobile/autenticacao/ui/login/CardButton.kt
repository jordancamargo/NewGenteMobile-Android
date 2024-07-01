package br.com.lg.MyWay.newgentemobile.autenticacao.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue900

@Composable
fun CardButton(iconId: Int, description: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            Modifier
                .padding(top = 8.dp, bottom = 4.dp)
                .background(
                    color = Color.White, shape = RoundedCornerShape(10.dp)
                )
                .padding(16.dp)
        )
        Text(
            text = description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Blue900
        )
    }
}