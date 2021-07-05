package kost.romi.timerforscreentimeout.data

import junit.framework.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TimerEntityTest {

    private lateinit var timerEntity: TimerEntity

    @Before
    fun setUp() {
        timerEntity =
            TimerEntity(System.currentTimeMillis(), 1000, 500, TimerState.STARTED, true)
    }

    @Test
    fun test_default_values() {
        assertEquals(1000, timerEntity.currentTime)
//        assertEquals(500, timerEntity.startAt)
    }

}