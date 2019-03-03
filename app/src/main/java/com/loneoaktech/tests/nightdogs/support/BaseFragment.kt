package com.loneoaktech.tests.nightdogs.support

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Created by BillH on 3/2/2019
 */
abstract class BaseFragment : Fragment(), CoroutineScope {

    private lateinit var rootJob: Job

    override val coroutineContext: CoroutineContext
        get() = rootJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootJob = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        rootJob.cancel()
    }
}