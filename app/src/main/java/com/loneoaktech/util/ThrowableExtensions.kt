package com.loneoaktech.util

/**
 * Created by BillH on 3/5/2019
 */

/**
 * Creates a short summary suitable for logging when showing the whole stack trace
 * isn't needed.
 */
fun Throwable.summary(): String =
    "class=${this::class.qualifiedName}, msg='${this.message}'"