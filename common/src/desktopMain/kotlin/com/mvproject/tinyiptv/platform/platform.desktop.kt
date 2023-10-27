/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 11:54
 *
 */

package com.mvproject.tinyiptv.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java

@Composable
actual fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle
): Font = Font("font/$res.ttf", weight, style)


actual fun createPlatformHttpClient(): HttpClient {
    // return HttpClient(OkHttp)
    return HttpClient(Java)
}