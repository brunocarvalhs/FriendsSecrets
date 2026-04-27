# Group List Feature
-keep class br.com.brunocarvalhs.group.list.** { *; }
-dontwarn br.com.brunocarvalhs.group.list.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
