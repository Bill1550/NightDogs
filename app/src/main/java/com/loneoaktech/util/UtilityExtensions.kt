package com.loneoaktech.util

/**
 * Created by BillH on 3/3/2019
 */

/**
* Runs the specified test on the subject, if true, returns the subject,
* if false, return null
*/
fun <T> T.nullIfFalse(predicate: (T) -> Boolean): T? =
    if (predicate(this)) this else null

/**
 * Runs the specified test on the subject, if false, returns the subject,
 * if false, return null
 */
fun <T> T.nullIfTrue(predicate: (T) -> Boolean): T? =
    if (predicate(this).not()) this else null


/**
 * Returns the string if not null and not blank,
 * returns null otherwise.
 */
fun String?.nullIfBlank() = if (this.isNullOrBlank()) null else this