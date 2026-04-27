# Chat Feature
-keep class br.com.brunocarvalhs.chat.** { *; }
-dontwarn br.com.brunocarvalhs.chat.**

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}
