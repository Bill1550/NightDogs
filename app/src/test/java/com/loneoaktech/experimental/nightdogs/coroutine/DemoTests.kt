package com.loneoaktech.experimental.nightdogs.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.lang.Thread.sleep
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/**
 * Created by BillH on 5/8/2019
 */
class DemoTests {

    @Test
    fun runACoroutine() {
        val counter = AtomicInteger(0)

        val time = measureTimeMillis {
            runBlocking {
                repeat(1000) {
                    launch {
                        counter.incrementAndGet()
                        delay(100)
                    }
                }
            }
        }

        System.out.println("total time=$time counter=${counter.get()}")
    }
}