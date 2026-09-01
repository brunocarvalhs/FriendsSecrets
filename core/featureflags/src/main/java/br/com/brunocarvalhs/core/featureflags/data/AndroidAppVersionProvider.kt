package br.com.brunocarvalhs.core.featureflags.data

import android.content.Context
import br.com.brunocarvalhs.core.featureflags.domain.AppVersionProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AndroidAppVersionProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AppVersionProvider {

    override fun getVersionName(): String = runCatching {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty()
    }.getOrDefault(EMPTY_VERSION)

    private companion object {
        const val EMPTY_VERSION = ""
    }
}
