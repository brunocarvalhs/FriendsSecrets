package br.com.brunocarvalhs.core.notifications.data

import br.com.brunocarvalhs.core.notifications.data.model.GroupSyncState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GroupSyncWorkerTest {

    @Test
    fun `shouldNotifyDrawCompleted returns false on first check`() {
        assertFalse(shouldNotifyDrawCompleted(previousState = null, hasDraw = true))
    }

    @Test
    fun `shouldNotifyDrawCompleted returns true when draw transitions to completed`() {
        val previousState = GroupSyncState(hasDraw = false)
        assertTrue(shouldNotifyDrawCompleted(previousState, hasDraw = true))
    }

    @Test
    fun `shouldNotifyDrawCompleted returns false when draw was already completed`() {
        val previousState = GroupSyncState(hasDraw = true)
        assertFalse(shouldNotifyDrawCompleted(previousState, hasDraw = true))
    }

    @Test
    fun `shouldNotifyDrawCompleted returns false when draw is still pending`() {
        val previousState = GroupSyncState(hasDraw = false)
        assertFalse(shouldNotifyDrawCompleted(previousState, hasDraw = false))
    }

    @Test
    fun `shouldNotifyNewMessage returns false on first check`() {
        assertFalse(shouldNotifyNewMessage(previousState = null, lastMessageTimestamp = 100L))
    }

    @Test
    fun `shouldNotifyNewMessage returns true when timestamp increased`() {
        val previousState = GroupSyncState(lastMessageTimestamp = 100L)
        assertTrue(shouldNotifyNewMessage(previousState, lastMessageTimestamp = 200L))
    }

    @Test
    fun `shouldNotifyNewMessage returns false when timestamp did not change`() {
        val previousState = GroupSyncState(lastMessageTimestamp = 100L)
        assertFalse(shouldNotifyNewMessage(previousState, lastMessageTimestamp = 100L))
    }
}
