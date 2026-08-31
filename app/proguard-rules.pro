# AndroidX / Compose / platform classes are NOT kept here: every AndroidX and
# Compose artifact ships its own consumer-rules.pro that AGP merges
# automatically, so a blanket app-level -keep only bloats the APK and blocks
# R8 from shrinking/obfuscating the largest chunk of the app's bytecode.

# Manter classes do Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Manter classes do Gson
-keep class com.google.gson.** { *; }
-keepattributes Signature,*Annotation*,InnerClasses

# Manter classes do Timber
-keep class com.jakewharton.timber.** { *; }

# Manter classes do Lottie
-keep class com.airbnb.lottie.** { *; }

# Regras de testes
-keep class org.junit.** { *; }
-keep class androidx.test.** { *; }

# Manter anotações de Gson
-keep @com.google.gson.annotations.SerializedName class * { *; }

# Kotlin Serialization
-keep,allowobfuscation,allowshrinking class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    @kotlinx.serialization.Serializer *;
}
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}

# Desativar avisos desnecessários
-dontwarn okhttp3.**
-dontwarn org.codehaus.mojo.animal_sniffer.**
-dontwarn androidx.window.extensions.**
-dontwarn androidx.window.sidecar.**

# Manter classes de navegação customizadas
-keep class br.com.brunocarvalhs.core.navigation.routers.** { *; }
-dontwarn br.com.brunocarvalhs.core.navigation.routers.**

# Hilt / Dagger
-keep class com.google.dagger.** { *; }
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.EntryPoint class *
-keep @dagger.hilt.InstallIn class *
-keep @dagger.Module class *
-keep @javax.inject.Inject class *

# Preserve Feature Initializers used in multibinding
-keep class * implements br.com.brunocarvalhs.core.navigation.FeatureInitializer { *; }
-keepclassmembers class * implements br.com.brunocarvalhs.core.navigation.FeatureInitializer {
    <init>(...);
}

# Segurança adicional: ofuscação agressiva para o resto do app
#-repackageclasses '' # Commeting this out as it often causes issues with DI and navigation
-allowaccessmodification

# Mantém o número da linha para stack traces legíveis via mapping.txt, mas
# troca o nome do arquivo-fonte real por "SourceFile" no APK final.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Para facilitar o diagnóstico
-printmapping mapping.txt
-verbose
