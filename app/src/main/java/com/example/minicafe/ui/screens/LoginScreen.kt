package com.example.minicafe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minicafe.R
import com.example.minicafe.ui.components.AppTextField
import com.example.minicafe.ui.components.PrimaryButton
import com.example.minicafe.ui.components.StatusBarIcons
import com.example.minicafe.ui.components.toast
import com.example.minicafe.ui.theme.Dark
import com.example.minicafe.ui.theme.Grey
import com.example.minicafe.ui.theme.InputBackground
import com.example.minicafe.ui.theme.TextSecondary

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    StatusBarIcons(darkIcons = false)

    val context = LocalContext.current
    val focus = LocalFocusManager.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun submit() {
        usernameError = if (username.isBlank()) "Entrez votre nom d'utilisateur" else null
        passwordError = if (password.length < 4) "4 caractères minimum" else null
        if (usernameError == null && passwordError == null) {
            focus.clearFocus()
            onLogin()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
            .verticalScroll(rememberScrollState()),
    ) {
        Image(
            painter = painterResource(R.drawable.img_login),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(214.dp),
        )
        Column(
            Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
        ) {
            Text("Bon retour 👋", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Dark)
            Spacer(Modifier.height(12.dp))
            Text("Connectez-vous pour commander votre café.", fontSize = 12.sp, color = Dark)

            Spacer(Modifier.height(20.dp))
            Text("Nom d'utilisateur", fontSize = 11.sp, color = Dark)
            Spacer(Modifier.height(6.dp))
            AppTextField(
                value = username,
                onValueChange = { username = it; usernameError = null },
                placeholder = "Votre nom d'utilisateur",
                error = usernameError,
                imeAction = ImeAction.Next,
            )

            Spacer(Modifier.height(8.dp))
            Text("Mot de passe", fontSize = 11.sp, color = Dark)
            Spacer(Modifier.height(6.dp))
            AppTextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                placeholder = "Votre mot de passe",
                isPassword = true,
                error = passwordError,
                imeAction = ImeAction.Done,
                onDone = { submit() },
            )

            Text(
                "Forgot Password?",
                fontSize = 11.sp,
                color = Dark,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { context.toast("Bientôt disponible") }
                    .padding(vertical = 4.dp),
            )

            Spacer(Modifier.height(12.dp))
            PrimaryButton("Se connecter", { submit() }, height = 48.dp, radius = 8.dp)

            Spacer(Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(Modifier.weight(1f), color = Grey)
                Text("  Or sign in with  ", fontSize = 11.sp, color = TextSecondary)
                HorizontalDivider(Modifier.weight(1f), color = Grey)
            }

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SocialButton("Google", R.drawable.ic_google, Modifier.weight(1f)) {
                    context.toast("Bientôt disponible")
                }
                SocialButton("Facebook", R.drawable.ic_facebook, Modifier.weight(1f)) {
                    context.toast("Bientôt disponible")
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                buildAnnotatedString {
                    append("Don't you have an account? ")
                    withStyle(SpanStyle(color = Color(0xFF2F5BEA))) { append("Sign up") }
                },
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { context.toast("Bientôt disponible") },
            )
        }
    }
}

@Composable
private fun SocialButton(label: String, icon: Int, modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(InputBackground)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(painterResource(icon), contentDescription = label, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 12.sp, color = Dark)
    }
}
