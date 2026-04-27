# Navigation Core
-keep class br.com.brunocarvalhs.core.navigation.** { *; }
-dontwarn br.com.brunocarvalhs.core.navigation.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
