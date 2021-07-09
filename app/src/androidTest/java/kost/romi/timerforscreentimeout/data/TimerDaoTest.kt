package kost.romi.timerforscreentimeout.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertFalse
import kost.romi.timerforscreentimeout.data.source.local.TimerDAO
import kost.romi.timerforscreentimeout.data.source.local.TimerDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TimerDaoTest {
    private lateinit var database: TimerDatabase
    private lateinit var dao: TimerDAO
    private var timerId: Long = 0

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, TimerDatabase::class.java).build()
        dao = database.timerDao()

        database.timerDao().insertAllTimerToHistoryTest(testTimer)
    }

    val testTimer = arrayListOf(
        TimerEntity(),
        TimerEntity(System.currentTimeMillis(), 500, 1000, TimerState.FINISH, false, 1)
    )

    @After
    fun closeDB() {
        database.close()
    }

    @Test
    fun testGetTimerHistory() = runBlocking {
        val timerEntity = TimerEntity(
            System.currentTimeMillis(),
            0,
            2000,
            TimerState.STOPPED,
            true,
            2
        ).also { timerId = 2 }
        dao.insertTimerToHistory(timerEntity)
        assertThat(dao.getAllTimerHistory().first().size, equalTo(2))
    }

    @Test
    fun testDeleteTimerHistory() = runBlocking {
        val timerEntity = TimerEntity(
            System.currentTimeMillis(),
            0,
            2000,
            TimerState.STOPPED,
            true,
            2
        ).also { timerId = 2 }
        dao.insertTimerToHistory(timerEntity)
        dao.deleteTable(timerEntity)
        assertThat(dao.getAllTimerHistory().first().size, equalTo(1))
    }
}