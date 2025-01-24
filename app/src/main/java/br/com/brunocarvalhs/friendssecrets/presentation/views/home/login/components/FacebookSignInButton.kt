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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.brunocarvalhs.friendssecrets.R

@Composable
fun FacebookSignInButton(
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
    onFacebookSignInClick: () -> Unit
) {
    Button(
        enabled = enabled,
        onClick = onFacebookSignInClick,
        modifier = modifier
            .apply {
                if (!isCompact) {
                    width(300.dp)
                }
            }
            .height(45.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(Color(0xFF1976d2)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_facebook),
                contentDescription = "Facebook icon",
                tint = Color.Unspecified,
            )
            if (!isCompact) {
                Text(
                    text = "Access using Facebook",
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
fun FacebookSignInButtonPreview() {
    FacebookSignInButton(onFacebookSignInClick = {})
}

@Composable
@Preview
fun FacebookSignInButtonCompactPreview() {
    FacebookSignInButton(isCompact = true) {

    }
}