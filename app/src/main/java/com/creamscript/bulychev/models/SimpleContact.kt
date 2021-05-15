package com.creamscript.bulychev.models

import androidx.annotation.DrawableRes

data class SimpleContact(
    val contactId: String,
    val contactName: String,
    val firstPhone: String,
    @DrawableRes val photoResId: Int
)