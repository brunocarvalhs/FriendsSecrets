# UI Core
-keep class br.com.brunocarvalhs.core.ui.** { *; }
-dontwarn br.com.brunocarvalhs.core.ui.**

# Keep Compose internal classes that might be needed
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material3.** { *; }
