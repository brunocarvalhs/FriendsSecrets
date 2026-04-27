# Keep serialization classes
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}

# Keep navigation routers
-keep class br.com.brunocarvalhs.core.navigation.routers.** { *; }
-dontwarn br.com.brunocarvalhs.core.navigation.routers.**
