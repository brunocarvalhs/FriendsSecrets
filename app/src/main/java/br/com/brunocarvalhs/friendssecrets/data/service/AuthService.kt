package br.com.brunocarvalhs.friendssecrets.data.service

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume

class AuthService(
    private val firebaseAuth: FirebaseAuth = Firebase.auth
) {

    suspend fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ): Result<Unit> {
        return suspendCancellableCoroutine { continuation ->
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Pode logar automaticamente se quiser:
                        // firebaseAuth.signInWithCredential(credential)
                        // ou simplesmente ignorar:
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        if (continuation.isActive) {
                            continuation.resume(Result.failure(e))
                        }
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        PhoneVerificationManager.store(verificationId)
                        if (continuation.isActive) {
                            continuation.resume(Result.success(Unit))
                        }
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    suspend fun signInWithCode(code: String): Result<Unit> {
        val id = PhoneVerificationManager.get()
            ?: return Result.failure(IllegalStateException("Verification ID is missing"))

        return try {
            val credential = PhoneAuthProvider.getCredential(id, code)
            firebaseAuth.signInWithCredential(credential).await()
            PhoneVerificationManager.clear()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        PhoneVerificationManager.clear()
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    private object PhoneVerificationManager {
        var verificationId: String? = null
            private set

        fun store(id: String) {
            verificationId = id
        }

        fun clear() {
            verificationId = null
        }

        fun get(): String? = verificationId
    }

}
