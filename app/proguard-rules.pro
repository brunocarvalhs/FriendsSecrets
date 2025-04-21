# Manter classes e métodos do Android
-keep class android.** { *; }
-keep class androidx.** { *; }

# Manter classes do ciclo de vida e ViewModel
-keep class androidx.lifecycle.** { *; }
-keepclassmembers class androidx.lifecycle.** { *; }
-keep class androidx.activity.** { *; }

# Manter classes do Jetpack Compose
-keep class androidx.compose.** { *; }
-keep class androidx.ui.** { *; }
-keep class androidx.material3.** { *; }

# Manter classes do Firebase
-keep class com.google.android.gms.** { *; }
-keep class com.firebase.** { *; }
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Manter classes do Gson
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Manter classes do Timber
-keep class com.jakewharton.timber.** { *; }

# Manter classes do Lottie
-keep class com.airbnb.lottie.** { *; }

# Manter classes do Generative AI (caso tenha anotações específicas)
-keep class com.generativeai.** { *; }

# Manter classes de navegação
-keep class androidx.navigation.** { *; }

# Manter classes de animação
-keep class androidx.compose.animation.** { *; }

# Regras de testes
-keep class org.junit.** { *; }
-keep class androidx.test.** { *; }

# Manter anotações de Retrofit (caso use)
-keepattributes *Annotation*

# Manter anotações de Gson
-keep @com.google.gson.annotations.SerializedName class * { *; }

# Desativar avisos desnecessários
-dontwarn okhttp3.**
-dontwarn org.codehaus.mojo.animal_sniffer.**

# Para facilitar o diagnóstico
-printmapping mapping.txt
-verbose
