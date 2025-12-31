package com.tridevs.serplusvita.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.tridevs.serplusvita.ui.theme.Inspiration

@Preview
@Composable
fun LogoSerPlusVita(
    tamano: Int = 48,
    colorSer: Color = Color(0xFF7FD959),
    colorVita: Color = Color(0xFF000000)
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = colorSer,
                    fontFamily = Inspiration,
                    fontSize = tamano.sp
                )
            ) { append("Ser+") }
            withStyle(
                style = SpanStyle(
                    color = colorVita,
                    fontFamily = Inspiration,
                    fontSize = tamano.sp
                )
            ) { append(" Vita") }
        }
    )
}