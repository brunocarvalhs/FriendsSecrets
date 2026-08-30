package br.com.brunocarvalhs.group.details.app.data.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

internal data class LinkMetadata(
    val title: String? = null,
    val imageUrl: String? = null,
)

internal class LinkMetadataFetcher @Inject constructor() {

    suspend fun fetch(url: String): LinkMetadata? = withContext(Dispatchers.IO) {
        runCatching {
            val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                connectTimeout = TIMEOUT_MS
                readTimeout = TIMEOUT_MS
                instanceFollowRedirects = true
                setRequestProperty("User-Agent", USER_AGENT)
            }

            connection.inputStream.bufferedReader().use { reader ->
                val buffer = CharArray(MAX_CHARS)
                val read = reader.read(buffer, 0, MAX_CHARS)
                val html = if (read > 0) String(buffer, 0, read) else ""
                parseMetadata(html)
            }
        }.getOrNull()
    }

    internal fun parseMetadata(html: String): LinkMetadata {
        val title = OG_TITLE_REGEX.find(html)?.groupValues?.get(1)
            ?: TITLE_TAG_REGEX.find(html)?.groupValues?.get(1)
        val image = OG_IMAGE_REGEX.find(html)?.groupValues?.get(1)

        return LinkMetadata(
            title = title?.let { decodeHtmlEntities(it).trim() }?.takeIf { it.isNotBlank() },
            imageUrl = image?.let { decodeHtmlEntities(it).trim() }?.takeIf { it.isNotBlank() },
        )
    }

    private fun decodeHtmlEntities(text: String): String = text
        .replace("&amp;", "&")
        .replace("&quot;", "\"")
        .replace("&#39;", "'")
        .replace("&lt;", "<")
        .replace("&gt;", ">")

    private companion object {
        const val TIMEOUT_MS = 4000
        const val MAX_CHARS = 65_536
        const val USER_AGENT = "Mozilla/5.0 (compatible; FriendsSecretsLinkPreview/1.0)"

        val OG_TITLE_REGEX = Regex(
            """<meta[^>]+property=["']og:title["'][^>]+content=["']([^"']*)["']""",
            RegexOption.IGNORE_CASE
        )
        val OG_IMAGE_REGEX = Regex(
            """<meta[^>]+property=["']og:image["'][^>]+content=["']([^"']*)["']""",
            RegexOption.IGNORE_CASE
        )
        val TITLE_TAG_REGEX = Regex("""<title[^>]*>([^<]*)</title>""", RegexOption.IGNORE_CASE)
    }
}
