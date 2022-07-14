package kz.putinbyte.iszhfermer.utils

import java.text.SimpleDateFormat
import java.util.*

object MyUtils {

    fun toMyDateTime(date: String): String {
        return try {
            date.substring(0,4) + date.substring(5,7) + date.substring(8,10)
        } catch (e: Exception) {
            ""
        }
    }

    fun toMyBirthDate(date: String): String {
        return try {
            date.substring(8,10) + "." +date.substring(5,7) + "."+ date.substring(0,4)
        } catch (e: Exception) {
            ""
        }
    }

    val currentDate: String = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

    fun toServerDateTime(date: String): String {
        return try {
            date.substring(0,4) + "." + date.substring(5,7) + "." + date.substring(8,10)
        } catch (e: Exception) {
            ""
        }
    }

    fun tuServerDate(string: String): String{
        return try {
            string.substring(6,10) + "-" + string.substring(3,5) + "-" + string.substring(0,2)
        } catch (e: Exception) {
            ""
        }
    }

    fun tuDesiredDateDay(number: Int): String{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, - number)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time).toString()
    }

    fun toMyDate(string: String?): String{
        val getServerDate = string?.substring(0,4) + "." + string?.substring(5,7) + "." + string?.substring(8,10)
        return try {
            getServerDate.substring(8,10) + "." + getServerDate.substring(5,7) + "." + getServerDate.substring(0,4)
        } catch (e: Exception) {
            ""
        }
    }
}