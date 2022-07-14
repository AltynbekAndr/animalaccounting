package kz.putinbyte.iszhfermer.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData

class CheckNetworkConnection(val context: Context) :
    LiveData<NetworkAvailability>() {

    companion object {
        private lateinit var instance: CheckNetworkConnection
        fun get(context: Context): CheckNetworkConnection {
            instance = if (::instance.isInitialized){
                instance
            }else{
                CheckNetworkConnection(context)
            }
            return instance
        }
    }

    private val connectivityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo

            value = if (netInfo != null && netInfo.isConnected) {
                NetworkAvailability.CONNECTED
            } else {
                NetworkAvailability.DISCONNECTED
            }
        }
    }
    override fun onActive() {
        super.onActive()
        value = NetworkAvailability.UNKNOWN
        val broadcastIntent = IntentFilter()
        broadcastIntent.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(connectivityBroadcastReceiver, broadcastIntent)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(connectivityBroadcastReceiver)
    }
}

enum class NetworkAvailability {
    UNKNOWN,
    CONNECTED,
    DISCONNECTED
}