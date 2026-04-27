# Group Details Feature
-keep class br.com.brunocarvalhs.group.details.** { *; }
-dontwarn br.com.brunocarvalhs.group.details.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
