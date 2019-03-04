package com.loneoaktech.util

import kotlinx.coroutines.*

/**
 * Created by BillH on 3/4/2019
 */

/**
 * Compact function to run a suspending method and capturing the errors.
 */
fun CoroutineScope.launchWithUx(
        busyIndicator: ((Boolean)->Unit)?,
        errorHandler: ((Throwable)->Unit)?,
        block: suspend ()->Unit
): Job {

    return launch(Dispatchers.Main) {
        try {
            busyIndicator?.invoke(true)
            block()
        } catch (ce: CancellationException){
            // ignore
        } catch (t: Throwable) {
            errorHandler?.invoke(t)
        } finally {
            busyIndicator?.invoke(false)
        }
    }

}
