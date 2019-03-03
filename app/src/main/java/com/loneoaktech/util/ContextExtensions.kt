package com.loneoaktech.util

import android.content.Context
import android.widget.Toast
import timber.log.Timber

/**
 * Created by BillH on 3/3/2019
 */

/**
 * Convenience function to show a toast from a possibly null context.
 */
fun Context?.toast( message: CharSequence, duration: Int = Toast.LENGTH_LONG ) {
    this?.let { ctx ->
        Toast.makeText(ctx, message, duration).show()
    } ?: Timber.e("Toast request on null context, msg=$message")
}