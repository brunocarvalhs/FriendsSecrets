# Group Create Feature
-keep class br.com.brunocarvalhs.group.create.** { *; }
-dontwarn br.com.brunocarvalhs.group.create.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
