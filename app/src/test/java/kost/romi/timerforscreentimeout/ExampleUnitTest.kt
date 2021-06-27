package kost.romi.timerforscreentimeout

import android.os.CountDownTimer
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * TODO: do test DB.
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    var countDown = 10

    @Test
    fun tickerTest() {
        runBlocking { ticerMethod() }
    }

    suspend fun ticerMethod() {
        while (countDown >= 0) {
            delay(1000L)
            println("$countDown")
            countDown--
        }
    }
}