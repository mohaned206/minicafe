package com.example.minicafe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minicafe.R
import com.example.minicafe.ui.components.PrimaryButton
import com.example.minicafe.ui.components.StatusBarIcons

@Composable
fun OnboardingScreen(onStart: () -> Unit) {
    StatusBarIcons(darkIcons = false)

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        Image(
            painter = painterResource(R.drawable.img_onboarding),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75f),
        )
        Box(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    0.45f to Color.Transparent,
                    0.70f to Color.Black,
                )
            )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Un café frais, à portée de main",
                color = Color.White,
                fontSize = 32.sp,
                lineHeight = 44.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Bienvenue chez MiniCafé, où chaque tasse est préparée rien que pour vous.",
                color = Color(0xFFA9A9A9),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(32.dp))
            PrimaryButton(text = "Commencer", onClick = onStart)
        }
    }
}
