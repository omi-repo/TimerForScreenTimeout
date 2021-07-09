package kost.romi.timerforscreentimeout.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.reactivex.internal.util.NotificationLite.getValue
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kost.romi.timerforscreentimeout.MainCoroutineRule
import kost.romi.timerforscreentimeout.data.TimerDataRepository
import kost.romi.timerforscreentimeout.data.TimerEntity
import kost.romi.timerforscreentimeout.data.TimerState
import kost.romi.timerforscreentimeout.data.source.local.TimerDatabase
import kost.romi.timerforscreentimeout.runBlockingTest
import kost.romi.timerforscreentimeout.timerdetail.SetTimerViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import javax.inject.Inject

@HiltAndroidTest
class SetTimerViewModelTest {
    private lateinit var database: TimerDatabase
    private lateinit var viewmodel: SetTimerViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)
        .around(coroutineRule)

    @Inject
    lateinit var repository: TimerDataRepository

    @Before
    fun setUp() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, TimerDatabase::class.java).build()

        val timerEntity = TimerEntity(
            System.currentTimeMillis(),
            0,
            2000,
            TimerState.STOPPED,
            true,
            2
        )

        val savedStateHandle: SavedStateHandle = SavedStateHandle().apply {
            set("timerId", timerEntity.id)
        }
        viewmodel = SetTimerViewModel(savedStateHandle, repository)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @kotlin.jvm.Throws(InterruptedException::class)
    fun testDefaultValues() = coroutineRule.runBlockingTest {
        assertTrue(getValue(viewmodel.timerState.currentTime), equals(0))
    }
}