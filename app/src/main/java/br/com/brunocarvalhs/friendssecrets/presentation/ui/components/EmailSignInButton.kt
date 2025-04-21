package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmailSignInButton(onEmailSignInClick: () -> Unit) {
    Button(
        onClick = onEmailSignInClick,
        modifier = Modifier
            .width(300.dp)
            .height(45.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email icon",
            )
            Text(
                text = "Access using Email",
                fontWeight = FontWeight.W600,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
@Preview
fun EmailSignInButtonPreview() {
    EmailSignInButton(onEmailSignInClick = {})
}