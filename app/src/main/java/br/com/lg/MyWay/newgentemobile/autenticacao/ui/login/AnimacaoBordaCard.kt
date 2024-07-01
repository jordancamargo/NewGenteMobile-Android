package br.com.lg.MyWay.newgentemobile.autenticacao.ui.login

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue500
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue200
import br.com.lg.MyWay.newgentemobile.ui.theme.Blue700

@Composable
fun AnimacaoBordaCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(size = 0.dp),
    borderWidth: Dp = 4.dp,
    gradient: Brush = Brush.sweepGradient(listOf(Blue500, Blue200)),
    animatonDuration: Int = 10000,
    content: @Composable () -> Unit
    ) {
        val transicaoInfinita = rememberInfiniteTransition(label = "Animação de cores infinitas")
        val graus by transicaoInfinita.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = animatonDuration, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Cores infinitas"
        )
    Surface(
        modifier = modifier,
        shape = shape
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .padding(borderWidth)
                .drawWithContent {
                    rotate(degrees = graus) {
                        drawCircle(
                            brush = gradient,
                            radius = size.width,
                            blendMode = BlendMode.SrcOver
                        )
                    }
                    drawContent()
                },
            color = Blue700,
            shape = shape
        ) {
            content()
        }
    }
}

@Composable
@Preview
private fun AnimacaoBordaCardPreview() {
    AnimacaoBordaCard (
        modifier = Modifier.height(200.dp).fillMaxSize(),
        shape = RoundedCornerShape(30.dp),
        borderWidth = 4.dp,
        gradient = Brush.sweepGradient(
            listOf(
                Blue500,
                Blue200
            )
        ),
        animatonDuration = 10000
    ){

    }
}