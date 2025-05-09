package com.example.vetJa.utils

import android.content.Context
import android.widget.Toast

fun toast(msg: String, ctx: Context) {
    Toast.makeText(
        ctx,
        msg,
        Toast.LENGTH_LONG
    ).show()
}