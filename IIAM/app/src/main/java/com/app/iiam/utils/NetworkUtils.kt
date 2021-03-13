package com.app.iiam.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import com.app.iiam.IIAM
import timber.log.Timber


class NetworkUtils {

    companion object {

        val isConnected: Boolean
            get() {
                val info = networkInfo
                return info != null && info.isAvailable && info.isConnected
            }


        val isConnectedOnSlowNetwork: Boolean
            get() {
                val info = networkInfo
                if (info == null || !info.isConnected) {
                    Timber.w("isConnectedOnSlowNetwork : device not connected to network. Check with isConnected() first!")
                    return true
                }
                return !hasFastConnection(info.type, info.subtype)
            }

        private val networkInfo: NetworkInfo?
            private get() {
                val connectivityManager =
                    IIAM.instance.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                return connectivityManager.activeNetworkInfo
            }

        private fun hasFastConnection(type: Int, subType: Int): Boolean {
            return if (type == ConnectivityManager.TYPE_WIFI) {
                true
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                when (subType) {
                    TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                    TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
                    TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                    TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
                    TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                    TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
                    TelephonyManager.NETWORK_TYPE_EHRPD -> true // ~ 1-2 Mbps
                    TelephonyManager.NETWORK_TYPE_EVDO_B -> true // ~ 5 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPAP -> true // ~ 10-20 Mbps
                    TelephonyManager.NETWORK_TYPE_IDEN -> false // ~25 kbps
                    TelephonyManager.NETWORK_TYPE_LTE -> true // ~ 10+ Mbps
                    TelephonyManager.NETWORK_TYPE_UNKNOWN -> {
                        Timber.w("hasFastConnection : Unrecognized sub type of network. Returning false")
                        false
                    }
                    else -> {
                        Timber.w("hasFastConnection : Unrecognized sub type of network. Returning false")
                        false
                    }
                }
            } else {
                false
            }
        }
    }
}