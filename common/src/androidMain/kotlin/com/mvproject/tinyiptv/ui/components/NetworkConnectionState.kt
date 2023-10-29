/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 28.10.23, 15:16
 *
 */

package com.mvproject.tinyiptv.ui.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun networkConnectionState(
    context: Context = LocalContext.current,
): State<ConnectionState> {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val initialState: ConnectionState = ConnectionState.Available

    fun observeConnectivityAsFlow() = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Napier.w("testing observeConnectivityAsFlow onAvailable")
                launch { send(ConnectionState.Available) }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                Napier.w("testing observeConnectivityAsFlow onLosing")
                launch { send(ConnectionState.Unavailable) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Napier.w("testing observeConnectivityAsFlow onLost")
                launch { send(ConnectionState.Unavailable) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Napier.w("testing observeConnectivityAsFlow onUnavailable")
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