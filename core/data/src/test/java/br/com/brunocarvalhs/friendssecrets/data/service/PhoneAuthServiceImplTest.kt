package br.com.brunocarvalhs.friendssecrets.data.service

import android.app.Activity
import br.com.brunocarvalhs.friendssecrets.domain.services.PhoneAuthService
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PhoneAuthServiceImplTest {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var service: PhoneAuthService
    private lateinit var activity: Activity

    @Before
    fun setUp() {
        firebaseAuth = mockk(relaxed = true)
        activity = mockk(relaxed = true)
        mockkConstructor(PhoneAuthOptions.Builder::class)
        service = PhoneAuthServiceImpl(firebaseAuth)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `sendVerificationCode returns failure if not Activity`() = runTest {
        val result = service.sendVerificationCode("+5511999999999", "notActivity")
        assertTrue(result.isFailure)
    }

    @Test
    fun `sendVerificationCode triggers verification failed`() = runTest {
        val builder = mockk<PhoneAuthOptions.Builder>(relaxed = true)
        val options = mockk<PhoneAuthOptions>(relaxed = true)
        val callbackSlot = slot<PhoneAuthProvider.OnVerificationStateChangedCallbacks>()
        every { anyConstructed<PhoneAuthOptions.Builder>().setPhoneNumber(any()) } returns builder
        every { builder.setTimeout(any<Long>(), any()) } returns builder
        every { builder.setActivity(any()) } returns builder
        every { builder.setCallbacks(capture(callbackSlot)) } returns builder
        every { builder.build() } returns options
        every { PhoneAuthProvider.verifyPhoneNumber(options) } answers {
            callbackSlot.captured.onVerificationFailed(FirebaseException("fail"))
        }
        val result = service.sendVerificationCode("+5511999999999", activity)
        assertTrue(result.isFailure)
    }
}

