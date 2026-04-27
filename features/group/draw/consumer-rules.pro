# Group Draw Feature
-keep class br.com.brunocarvalhs.group.draw.** { *; }
-dontwarn br.com.brunocarvalhs.group.draw.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
