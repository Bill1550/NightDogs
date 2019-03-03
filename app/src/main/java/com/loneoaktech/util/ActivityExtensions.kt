package com.loneoaktech.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

/**
 * Created by BillH on 3/2/2019
 */

/**
 * Simple method that wraps a fragment transaction so that it is committed at the end.
 */
fun AppCompatActivity.fragmentTransact( block: FragmentTransaction.()->Unit ) {
    supportFragmentManager.beginTransaction().apply {
        block.invoke(this)
    }.commit()
}

