package com.loneoaktech.experimental.nightdogs.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.lang.Thread.sleep
import kotlin.concurrent.thread

/**
 * Created by BillH on 6/5/2019
 */
class CrashAndBurnDemo {

    @Test
    fun helloWorld() {

        System.out.println( getWelcomeMessage(0) )
    }


    fun getWelcomeMessage(i: Int) = "Hello Maven $i"

    @Test
    fun delayedHello() {

         runBlocking {

             repeat(3) { i ->
                 launch {
                     delay(3000)
                     println(getWelcomeMessage(i))
                 }
             }

             println("Waiting for message")
        }

        println("bye")
    }

    @Test
    fun threadedHello() {

        val threads = (0..2).map {
            thread {
                sleep(3000)
                println(getWelcomeMessage(0))
            }
        }

        println( "Waiting....")

        threads.forEach { it.join() }

        sleep(3000)
        println(" bye")
    }
}