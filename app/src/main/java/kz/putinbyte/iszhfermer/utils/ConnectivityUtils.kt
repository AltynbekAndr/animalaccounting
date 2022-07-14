package kz.putinbyte.iszhfermer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object ConnectivityUtils {
    /**
     * Get the network info
     * @param context
     * @return
     */
    fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    fun isConnected(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * Lack of internet traffic
     * @param context
     * @return
     */
    fun isTrafficInternet(): Boolean {
        try {
            val url: HttpURLConnection = URL("https://www.google.ru/").openConnection() as HttpURLConnection
            url.setRequestProperty("User-Agent", "Test")
            url.setRequestProperty("Connection", "close")
            url.connectTimeout = 1500
            url.connect()
            return url.responseCode == 200
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @param type
     * @return
     */
    fun isConnectedWifi(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @param type
     * @return
     */
    fun isConnectedMobile(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    fun isConnectedFast(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && isConnectionFast(info.type, info.subtype)
    }

    @SuppressLint("NewApi", "ObsoleteSdkInt")
    fun getNetworkUpSpeed(context: Context): Int {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.getNetworkCapabilities(cm.activeNetwork)?.linkUpstreamBandwidthKbps?.div(1024) ?: 0
        } else {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cm.getNetworkCapabilities(cm.activeNetwork)?.linkUpstreamBandwidthKbps?.div(1024)
                    ?: 0
            } else {
                0
            }
        }
    }

    fun syncAvailability(context: Context, dataType: DataType = DataType.META) =
        when (dataType) {
            DataType.FILE -> getNetworkUpSpeed(context) > 2
            DataType.META -> getNetworkUpSpeed(context) >= 1
        }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    fun isConnectionFast(type: Int, subType: Int): Boolean {
        return if (type == ConnectivityManager.TYPE_WIFI) {
            true
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            when (subType) {
                TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> false // ~ 400-1000 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_A -> false // ~ 600-1400 kbps
                TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                TelephonyManager.NETWORK_TYPE_HSPA -> false // ~ 700-1700 kbps
                TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
                TelephonyManager.NETWORK_TYPE_EHRPD -> true // ~ 1-2 Mbps
                TelephonyManager.NETWORK_TYPE_EVDO_B -> true // ~ 5 Mbps
                TelephonyManager.NETWORK_TYPE_HSPAP -> true // ~ 10-20 Mbps
                TelephonyManager.NETWORK_TYPE_IDEN -> false // ~25 kbps
                TelephonyManager.NETWORK_TYPE_LTE -> true // ~ 10+ Mbps
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                else -> false
            }
        } else {
            false
        }
    }

    enum class DataType {
        META,
        FILE
    }
}