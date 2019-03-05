package com.loneoaktech.util

/**
 * Created by BillH on 7/9/2018
 */

/**
 * Lazy initializer that accepts a parameter.
 *
 * typical usage:
 *
 * class Manager private constructor(uiContext: Context) {
 *      init {
 *               // Init using uiContext argument
 *      }
 *
 *       companion object : SingletonHolder<Manager, Context>(::Manager)
 * }
 *
 * Manager.getInstance( art ).someFunction
 *
 * from: https://medium.com/@BladeCoder/kotlin-singletons-with-argument-194ef06edd9e
 *
 */
open class SingletonHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}