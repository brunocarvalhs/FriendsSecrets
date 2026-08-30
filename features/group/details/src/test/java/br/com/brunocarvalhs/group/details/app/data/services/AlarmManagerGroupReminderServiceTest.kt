package br.com.brunocarvalhs.group.details.app.data.services

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import br.com.brunocarvalhs.core.domain.model.GroupModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class AlarmManagerGroupReminderServiceTest {

    private lateinit var service: AlarmManagerGroupReminderService

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        service = AlarmManagerGroupReminderService(context)
    }

    @Test
    fun `schedule should return false when date is blank`() {
        val group = GroupModel(id = "group-1", date = "")

        assertFalse(service.schedule(group))
    }

    @Test
    fun `schedule should return false when date is null`() {
        val group = GroupModel(id = "group-1", date = null)

        assertFalse(service.schedule(group))
    }

    @Test
    fun `schedule should return false when date is not parseable`() {
        val group = GroupModel(id = "group-1", date = "not-a-date")

        assertFalse(service.schedule(group))
    }

    @Test
    fun `schedule should return false when date is in the past`() {
        val pastDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().apply { add(Calendar.YEAR, -1) }.time)
        val group = GroupModel(id = "group-1", date = pastDate)

        assertFalse(service.schedule(group))
    }

    @Test
    fun `schedule should return true when date is valid and in the future`() {
        val futureDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().apply { add(Calendar.YEAR, 1) }.time)
        val group = GroupModel(id = "group-1", date = futureDate)

        assertTrue(service.schedule(group))
    }

    @Test
    fun `cancel should not throw when there is nothing scheduled`() {
        val group = GroupModel(id = "group-1", date = null)

        service.cancel(group)
    }
}
