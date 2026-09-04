package br.com.brunocarvalhs.core.review.data

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

/**
 * Launches Google Play's in-app review flow, which shows the rating dialog
 * without leaving the app. Play decides internally whether to actually show
 * it (it caps how often a given user can see it, regardless of how often
 * this is called), so callers cannot know if a rating was actually left.
 *
 * Falls back to opening the Play Store listing directly if the in-app flow
 * fails to load (e.g. Play Store app missing, as on some emulators).
 */
class InAppReviewLauncher @Inject constructor() {

    suspend fun launch(activity: Activity) {
        runCatching {
            val reviewManager = ReviewManagerFactory.create(activity)
            val request = reviewManager.requestReviewFlow().await()
            reviewManager.launchReviewFlow(activity, request).await()
        }.onFailure { error ->
            Timber.e(error, "In-app review flow failed, falling back to Play Store listing")
            openPlayStoreListing(activity)
        }
    }

    private fun openPlayStoreListing(activity: Activity) {
        val packageName = activity.packageName
        val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        if (marketIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(marketIntent)
        } else {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
            activity.startActivity(webIntent)
        }
    }
}
