package com.loneoaktech.experimental.nightdogs.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.lang.Thread.sleep
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/**
 * Created by BillH on 5/8/2019
 */
class CoroutinesVsThreadsComparison {

    @Test
    fun runCoroutines() {
        runBlocking { launch { delay(1) } }

        val counter = AtomicInteger(0)
        val runTime = measureTimeMillis {

            runBlocking {

                repeat(10_000) {
                    launch {
                        delay( 100 )
                        counter.incrementAndGet()
                    }
                }

            }
        }

        System.out.println("Run time = $runTime, loops=${counter.get()}")
    }


    @Test
    fun runThreads() {

        val counter = AtomicInteger(0)
        val runTime = measureTimeMillis {

            (0 until 10_000).map {
                thread {
                    sleep(100)
                    counter.incrementAndGet()
                }
            }.forEach { thread ->
                thread.join()
            }

        }

        System.out.println("Run time = $runTime, loops=${counter.get()}")
    }
}