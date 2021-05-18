package com.creamscript.bulychev.views

import com.creamscript.bulychev.models.SimpleContact

interface ContactSelectable {
    fun contactSelected(simpleContact: SimpleContact)
}