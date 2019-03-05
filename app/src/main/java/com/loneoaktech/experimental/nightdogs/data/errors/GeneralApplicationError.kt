package com.loneoaktech.experimental.nightdogs.data.errors

import android.content.Context
import androidx.annotation.StringRes
import com.loneoaktech.util.nullIfBlank
import com.loneoaktech.util.nullIfTrue

/**
 * Created by BillH on 3/3/2019
 */
class GeneralApplicationError(
    val displayMessage: String,
    debugMessage: String
) : Exception( debugMessage )

fun Context?.throwApplicationError(debugMessage: String, @StringRes msgId: Int? = null ): Nothing {
    throw GeneralApplicationError(

        displayMessage = this?.let { ctx ->
            msgId?.nullIfTrue { it == 0}?.let { ctx.getString(it).nullIfBlank() }
        } ?: debugMessage,

        debugMessage = debugMessage
    )
}