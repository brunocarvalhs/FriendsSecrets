# Biometric Feature
-keep class br.com.brunocarvalhs.biometric.** { *; }
-dontwarn br.com.brunocarvalhs.biometric.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
