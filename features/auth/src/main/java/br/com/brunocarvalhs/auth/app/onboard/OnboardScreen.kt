package br.com.brunocarvalhs.auth.app.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard.OnboardViewModel
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch


@Composable
fun OnboardingScreen(
    navController: NavHostController,
    initialPage: Int = 0,
    viewModel: OnboardViewModel = viewModel(
        factory = OnboardViewModel.Factory
    ),
) {
    val uiState: OnboardUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(OnboardViewIntent.FetchData)
    }

    when (uiState) {
        is OnboardUiState.Success -> {
            OnboardingContent(
                navController = navController,
                initialPage = initialPage,
                pages = (uiState as OnboardUiState.Success).onboard
            )
        }

        is OnboardUiState.Idle -> {
            OnboardingContent(
                navController = navController,
                initialPage = initialPage,
                pages = (uiState as OnboardUiState.Idle).onboard
            )
        }

        OnboardUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun OnboardingContent(
    navController: NavHostController,
    initialPage: Int = 0,
    pages: List<OnboardViewModel.Onboarding> = emptyList(),
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        initialPageOffsetFraction = 0f,
        pageCount = { pages.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.onboarding_screen_close)
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                val currentPage = pages[page]
                OnboardingPage(
                    imageUrl = currentPage.imageSource,
                    title = currentPage.title,
                    description = currentPage.description
                )
            }

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pages.size) { index ->
                    val color =
                        if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary else Color.Gray
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (pagerState.currentPage < pages.size - 1) {
                    TextButton(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }) {
                        Text(text = stringResource(R.string.onboarding_next), fontSize = 16.sp)
                    }
                } else {
                    Button(onClick = {
                        navController.popBackStack()
                    }) {
                        Text(text = stringResource(R.string.onboarding_finish), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    imageUrl: OnboardViewModel.ImageSource,
    title: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (imageUrl) {
            is OnboardViewModel.ImageSource.Url -> {
                SubcomposeAsyncImage(
                    model = imageUrl.url,
                    contentDescription = title,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Inside,
                    loading = {
                        CircularProgressIndicator()
                    }
                )
            }

            is OnboardViewModel.ImageSource.Resource -> {
                Image(
                    painter = painterResource(id = imageUrl.resId),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit
                )
            }

            is OnboardViewModel.ImageSource.Animation -> {
                val composition: LottieComposition? by rememberLottieComposition(
                    LottieCompositionSpec.Url(imageUrl.json)
                )

                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )

                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier.size(200.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}

private class OnboardingPreviewProvider : PreviewParameterProvider<OnboardUiState> {
    override val values = sequenceOf(
        OnboardUiState.Idle(onboard = OnboardViewModel.default),
        OnboardUiState.Success(onboard = OnboardViewModel.default),
        OnboardUiState.Loading
    )
}

@Preview
@Composable
fun OnboardingContentPreview(
    @PreviewParameter(OnboardingPreviewProvider::class) page: Int,
) {
    FriendsSecretsTheme {
        OnboardingContent(navController = rememberNavController(), initialPage = page)
    }
}
