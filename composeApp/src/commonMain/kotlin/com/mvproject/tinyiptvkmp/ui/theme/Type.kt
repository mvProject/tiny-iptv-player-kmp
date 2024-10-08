/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 10:49
 *
 */

package com.mvproject.tinyiptvkmp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.montserrat_bold
import tinyiptvkmp.composeapp.generated.resources.montserrat_extrabold
import tinyiptvkmp.composeapp.generated.resources.montserrat_medium
import tinyiptvkmp.composeapp.generated.resources.montserrat_regular
import tinyiptvkmp.composeapp.generated.resources.montserrat_semibold

@Composable
fun getTypography(): Typography {
    val fonts =
        FontFamily(
            Font(Res.font.montserrat_regular, FontWeight.Normal, FontStyle.Normal),
            Font(Res.font.montserrat_medium, FontWeight.Medium, FontStyle.Normal),
            Font(Res.font.montserrat_semibold, FontWeight.SemiBold, FontStyle.Normal),
            Font(Res.font.montserrat_bold, FontWeight.Bold, FontStyle.Normal),
            Font(Res.font.montserrat_extrabold, FontWeight.ExtraBold, FontStyle.Normal),
        )

    return Typography(
        displayLarge =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.1.sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.1.sp,
            ),
        displaySmall =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.1.sp,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.2.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.2.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.1.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.2.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.3.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.3.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.3.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                letterSpacing = 0.5.sp,
            ),
    )
}
