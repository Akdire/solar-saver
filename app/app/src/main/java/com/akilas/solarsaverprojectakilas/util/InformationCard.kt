
package com.akilas.solarsaverprojectakilas.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team14.solarsaver.R

@Composable
fun InformationCard(
    title: String,
    image: Int? = null,
    information: String,
) {
    Card(
        modifier = Modifier.width(225.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(20F, TextUnitType.Sp)
            )

            if (image != null) {
                Box(content = {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = "SolarSaver sin logo med tekst under",
                        modifier = Modifier.fillMaxWidth()
                    )
                })
            }

            Spacer(
                modifier = Modifier.padding(7.dp)
            )

            Text(text = information, textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun InformationCardPreview() {
    InformationCard(
        title = "SolarSaver sin logo",
        image = R.drawable.solarsaver_med_tekst,
        information = "Test for å se hvordan dette funker\nSjekk hva som skjer hvis jeg skriver en lang setning"
    )
}
