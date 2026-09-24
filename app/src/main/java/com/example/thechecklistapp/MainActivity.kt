package com.example.thechecklistapp

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.thechecklistapp.device.ConnectivityStatus
import com.example.thechecklistapp.device.ConnectivityStatusPublisher
import com.example.thechecklistapp.ui.navigation.SetupNavGraph
import com.example.thechecklistapp.ui.theme.TheChecklistAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityStatusPublisher by inject<ConnectivityStatusPublisher>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupConnectivityMonitoring()
        enableEdgeToEdge()
        setContent {
            TheChecklistAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    override fun onStop() {
        connectivityManager.unregisterNetworkCallback(networkCallback)

        super.onStop()
    }

    private fun setupConnectivityMonitoring() {
        connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                lifecycleScope.launch(Dispatchers.Default) {
                    connectivityStatusPublisher.publishStatus(ConnectivityStatus.CONNECTED)
                }
            }

            override fun onLost(network: Network) {
                lifecycleScope.launch(Dispatchers.Default) {
                    connectivityStatusPublisher.publishStatus(ConnectivityStatus.DISCONNECTED)
                }
            }
        }
    }
}
