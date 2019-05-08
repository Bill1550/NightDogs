package com.loneoaktech.experimental.nightdogs.coroutine

import kotlinx.coroutines.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.Thread.sleep
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/**
 * Created by BillH on 5/8/2019
 */
class ManyManyExample {

    /**
     * Run a bunch of coroutines concurrently and return the execution time.
     */
    private fun runCoroutines(num: Int, stuff: suspend () -> Unit): Long =
        measureTimeMillis {
            runBlocking {
                repeat(num) {
                    launch { stuff() }
                }
            } // automatically waits for coroutines to complete
        }

    /**
     * Run a bunch of threads concurrently and return the total execution time
     */
    private fun runThreads( num: Int, stuff: ()->Unit ): Long {
        return measureTimeMillis {
            (0 until num).map {
                thread {
                    stuff()
                }
            }.map { thread ->
                thread.join()
            }
        }
    }

    @Test
    fun runManyCoroutines() = runBlocking {

        val numJobs = 1_000
        val counter = AtomicInteger(0)

        System.out.println("Running coroutines: $numJobs")

        runCoroutines(numJobs) {
            delay(100)
            counter.incrementAndGet()
        }.let { time ->
            System.out.println("Run time=$time msec, loops=${counter.get()}")
        }
    }


    @Test
    fun runManyThreads() {

        val numThreads = 1_000

        val counter = AtomicInteger(0)

        System.out.println("Running threads: $numThreads")
        runThreads(numThreads) {
            sleep(100)
            counter.incrementAndGet()
        }.let { time ->
            System.out.println("Time=$time, counter=${counter.get()}")
        }
    }

    @Test
    fun compareCoroutinesAndThreads() {

        runBlocking { delay(10) } // initialize the coroutine system so that time isn't in sampls below

        val counter = AtomicInteger(0)
        val delayTime = 100L


        listOf( 100, 1_000, 10_000, 100_000).forEach { num ->
            counter.set(0)
            val threadTime = runThreads(num){
                sleep(delayTime)
                counter.incrementAndGet()
            }
            assertEquals(num, counter.get())

            counter.set(0)
            val coroutineTime = runCoroutines(num) {
                delay(delayTime)
                counter.incrementAndGet()
            }
            assertEquals(num, counter.get())

            System.out.println("num=$num, thread time=$threadTime, coroutine time=$coroutineTime")
        }
    }
}