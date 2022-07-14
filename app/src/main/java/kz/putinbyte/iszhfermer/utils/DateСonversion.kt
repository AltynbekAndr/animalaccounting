package kz.putinbyte.iszhfermer.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun millisecondsTodDate(timestamp: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(Date(timestamp.toLong()))
}

fun getCalculatedDate(dateFormat: String?, days: Int): String? {
    val cal = Calendar.getInstance()
    val s = SimpleDateFormat(dateFormat)
    cal.add(Calendar.DAY_OF_YEAR, days)
    return s.format(Date(cal.timeInMillis))
}

fun milliseconds(date: String?): Long {
    //String date_ = date;
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    try {
        val mDate = sdf.parse(date)
        val timeInMilliseconds = mDate.time
        return timeInMilliseconds
    } catch (e: ParseException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }
    return 0
}

fun dateTime(): String{
    val currentDate = SimpleDateFormat("dd.MM.yyyy ", Locale.getDefault()).format(Date())
    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    return currentDate + currentTime
}