package com.loneoaktech.experimental.nightdogs.coroutine

import kotlinx.coroutines.*
import org.junit.Test

/**
 * Created by BillH on 6/5/2019
 */
class DownInFlames {

    @Test
    fun sayHello() {
        println(getMessage())
    }


    fun getMessage(i: Int = 0) = "Hello Maven Engineering $i"

    @Test
    fun lateHello() = runBlocking {

        repeat(3) { i ->
            launch {
//                delay(3000)i
//                System.out.println(getMessage(i))


                val msg = withContext( Dispatchers.Default){
                    getDelayedMessage(i)
                }


                val newMsg = msg + " again"
                System.out.println(newMsg)
            }
        }

        System.out.println("waitin....")
    }

    suspend fun getDelayedMessage(i: Int = -1 ): String {
        delay( 3000)
        return "Hello Engineering $i"
    }

}