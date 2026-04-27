# Settings Feature
-keep class br.com.brunocarvalhs.settings.** { *; }
-dontwarn br.com.brunocarvalhs.settings.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
