# UI Core
-keep class br.com.brunocarvalhs.core.ui.** { *; }
-dontwarn br.com.brunocarvalhs.core.ui.**

# androidx.compose.ui / androidx.compose.material3 are not kept here: those
# artifacts ship their own consumer-rules.pro, and an app/module-level
# blanket -keep only blocks R8 from shrinking and obfuscating them.
