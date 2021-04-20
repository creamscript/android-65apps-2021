package com.creamscript.bulychev.utils

import java.util.*

fun nextBirthday(calendar: Calendar): Calendar {

    /*
    * Возвращает календарь, на какое поставить следующее напоминание.
    * В случае, если выпадает на 29.02 в не високосный год возращает 28.02
    */

    val calendarNotify = calendar as GregorianCalendar
    var isFeb29  = false
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var currentCoDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

    calendarNotify[Calendar.DATE] = calendar[Calendar.DATE]
    calendarNotify[Calendar.MONTH] = calendar[Calendar.MONTH]
    calendarNotify[Calendar.YEAR] = calendar[Calendar.YEAR]
    calendarNotify[Calendar.HOUR] = 0
    calendarNotify[Calendar.MINUTE] = 0
    calendarNotify[Calendar.SECOND] = 0

    var calendarCoDay = calendar.get(Calendar.DAY_OF_YEAR)

    if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY
            && calendar.get(Calendar.DATE) == 29) {
        isFeb29 = true
    }

    if ((calendar.get(Calendar.MONTH) >= Calendar.MARCH
                    && calendar.isLeapYear(calendar.get(Calendar.YEAR))) || isFeb29) {
        calendarCoDay -= 1
    }

    if ((Calendar.getInstance().get(Calendar.MONTH) >= Calendar.MARCH
                    && calendar.isLeapYear(currentYear))
            || (Calendar.getInstance().get(Calendar.MONTH) == Calendar.FEBRUARY
                    && Calendar.getInstance().get(Calendar.DATE) == 29)) {
        currentCoDay -= 1
    }

    // если уже дата прошла ставим на следующий год
    if (currentCoDay >= calendarCoDay) {
        if (!calendar.isLeapYear(currentYear + 1) && isFeb29) {
            calendarNotify[Calendar.DATE] = 28
        }
        calendarNotify[Calendar.YEAR] = currentYear + 1
    } else {
        if (!calendar.isLeapYear(currentYear) && isFeb29) {
            calendarNotify[Calendar.DATE] = 28
        }
        calendarNotify[Calendar.YEAR] = currentYear
    }

    return calendarNotify
}