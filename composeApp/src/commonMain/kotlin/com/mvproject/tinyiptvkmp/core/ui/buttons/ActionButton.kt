package com.mvproject.tinyiptvkmp.core.ui.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.core.theme.dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.app_name

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {},
) {
    ElevatedButton(
        onClick = onClick,
        modifier =
            modifier
                .padding(MaterialTheme.dimens.size8)
                .fillMaxWidth(),
        colors =
            ButtonDefaults
                .buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
@Preview
private fun ActionButtonPreview() {
    VideoAppTheme {
        ActionButton(
            title = stringResource(Res.string.app_name),
        )
    }
}
