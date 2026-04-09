package br.com.brunocarvalhs.settings.commons.remembers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@Composable
fun rememberReviewRequester(): () -> Unit {
    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()

    fun openPlayStore(activity: Activity) {
        val packageName = activity.packageName
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(intent)
        } else {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
            activity.startActivity(webIntent)
        }
    }

    suspend fun requestInAppReview(activity: Activity) {
        try {
            val reviewManager = ReviewManagerFactory.create(activity)
            val request = reviewManager.requestReviewFlow().await()
            reviewManager.launchReviewFlow(activity, request).await()
        } catch (t: kotlinx.coroutines.CancellationException) {
            Timber.e(t)
            openPlayStore(activity)
        } catch (t: Exception) {
            Timber.e(t)
            openPlayStore(activity)
        }
    }

    return remember {
        {
            if (activity != null) {
                coroutineScope.launch {
                    openPlayStore(activity)
                }
            }
        }
    }
}
