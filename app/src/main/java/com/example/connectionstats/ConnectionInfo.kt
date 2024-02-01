package com.example.connectionstats

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.net.Inet4Address
import java.net.NetworkInterface

@Composable
fun ConnectionInfo(context: Context) {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = remember { connectivityManager.activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) } }
    val ipAddress = remember { getIpAddress() }
    Column {
//        Text("Connection Information:")
//        Text("Connected: ${networkCapabilities != null}")
        Text("IP Address: $ipAddress")

        // You can check for specific network capabilities if needed
        networkCapabilities?.let {
            Text("Network Type: ${getNetworkType(it)}")
            // Add more information as needed
        }
    }
}

fun getNetworkType(networkCapabilities: NetworkCapabilities): String {
    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
        else -> "Unknown"
    }
}

fun getIpAddress(): String {
    try {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            val inetAddresses = networkInterface.inetAddresses
            while (inetAddresses.hasMoreElements()) {
                val address = inetAddresses.nextElement()
                if (!address.isLoopbackAddress && address is Inet4Address) {
                    return address.hostAddress
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "Unknown"
}
