# Network & Firebase
-keep class br.com.brunocarvalhs.core.network.** { *; }
-dontwarn br.com.brunocarvalhs.core.network.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
