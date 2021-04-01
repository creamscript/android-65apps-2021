package com.creamscript.bulychev

import androidx.annotation.DrawableRes

data class ContactEntity (
    val contactName: String,
    val firstPhone: String,
    val secondPhone: String,
    val firstEmail: String,
    val secondEmail: String,
    val contactDescription: String,
    @DrawableRes val photoResId: Int,
)
