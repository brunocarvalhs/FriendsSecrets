package br.com.brunocarvalhs.group.details.app.data.services

private val HTTP_URL_REGEX = Regex("""^https?://\S+\.\S+""", RegexOption.IGNORE_CASE)

internal fun isLikelyUrl(text: String): Boolean {
    return HTTP_URL_REGEX.matches(text.trim())
}
