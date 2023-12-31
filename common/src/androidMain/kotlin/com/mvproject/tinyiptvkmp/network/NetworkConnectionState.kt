/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 17:18
 *
 */

package com.mvproject.tinyiptvkmp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
internal fun networkConnectionState(
    context: Context = LocalContext.current,
): State<ConnectionState> {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val initialState: ConnectionState = ConnectionState.Available

    fun observeConnectivityAsFlow() = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                KLog.w("testing observeConnectivityAsFlow onAvailable")
                launch { send(ConnectionState.Available) }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                KLog.w("testing observeConnectivityAsFlow onLosing")
                launch { send(ConnectionState.Unavailable) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                KLog.w("testing observeConnectivityAsFlow onLost")
                launch { send(ConnectionState.Unavailable) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                KLog.w("testing observeConnectivityAsFlow onUnavailable")
                launch { send(ConnectionState.Unavailable) }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    return produceState(initialValue = initialState) {
        // In a coroutine, can make suspend calls
        observeConnectivityAsFlow().collect {
            value = it
        }
    }
}

sealed class ConnectionState {
    data object Available : ConnectionState()
    data object Unavailable : ConnectionState()
}