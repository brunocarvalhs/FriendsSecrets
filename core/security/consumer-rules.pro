# Keep security related classes that might be used via reflection or needed for the app
-keep class br.com.brunocarvalhs.core.security.** { *; }

# If using Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
