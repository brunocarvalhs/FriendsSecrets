package br.com.brunocarvalhs.friendssecrets.presentation.views.home.login.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.brunocarvalhs.friendssecrets.R

@Composable
fun GoogleSignInButton(
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
    onGoogleSignInClick: () -> Unit
) {
    Button(
        enabled = enabled,
        onClick = onGoogleSignInClick,
        modifier = modifier
            .apply {
                if (!isCompact) {
                    width(300.dp)
                }
            }
            .height(45.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(White),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google icon",
                tint = Color.Unspecified,
            )
            if (!isCompact) {
                Text(
                    text = "Access using Google",
                    color = Black,
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun GoogleSignInButtonPreview() {
    GoogleSignInButton(onGoogleSignInClick = {})
}

@Composable
@Preview
fun GoogleSignInButtonCompactPreview() {
    GoogleSignInButton(isCompact = true, onGoogleSignInClick = {})
}