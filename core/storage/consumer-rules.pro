# Storage & DataStore
-keep class br.com.brunocarvalhs.storage.** { *; }
-dontwarn br.com.brunocarvalhs.storage.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
