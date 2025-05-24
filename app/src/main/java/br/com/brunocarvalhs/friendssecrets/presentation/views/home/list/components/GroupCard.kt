package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.MembersAvatar
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

private val imagesBottom = listOf(
    R.raw.card_bottom_one,
    R.raw.card_bottom_two,
    R.raw.card_top_one,
)

@Composable
fun GroupCard(
    modifier: Modifier = Modifier,
    group: GroupEntities,
    onClick: (GroupEntities) -> Unit,
    shape: Shape = CircleShape,
) {
    val image = rememberSaveable { imagesBottom.random() }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(image))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Card(
        modifier = modifier.height(200.dp),
        onClick = { onClick(group) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier
                    .size(200.dp)
                    .alpha(0.1f)
                    .align(Alignment.BottomEnd)
                    .offset(x = 40.dp, y = 40.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = group.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Column(
                    modifier = modifier.fillMaxHeight(0.8f),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    MembersAvatar(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        members = group.members,
                        shape = shape
                    )
                }
            }
        }
    }
}

@Composable
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
private fun GroupCardPreview() {
    FriendsSecretsTheme {
        GroupCard(
            group = GroupModel(
                name = "Living Room",
                members = listOf<UserEntities>().apply {
                    repeat(10) {
                        UserModel(
                            name = "Member $it",
                            likes = listOf("Like $it")
                        )
                    }
                },
            ),
            onClick = {}
        )
    }
}

@Composable
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
private fun GroupCardPreviewTwo() {
    FriendsSecretsTheme {
        GroupCard(
            group = GroupModel(
                name = "Living Room",
                members = listOf<UserEntities>().apply {
                    repeat(10) {
                        UserModel(
                            name = "Member $it",
                            likes = listOf("Like $it")
                        )
                    }
                },
            ),
            onClick = {}
        )
    }
}