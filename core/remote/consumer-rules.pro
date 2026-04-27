# Remote & Firebase Config
-keep class br.com.brunocarvalhs.core.remote.** { *; }
-dontwarn br.com.brunocarvalhs.core.remote.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
