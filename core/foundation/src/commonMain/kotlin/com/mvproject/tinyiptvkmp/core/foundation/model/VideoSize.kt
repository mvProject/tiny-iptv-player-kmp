package com.mvproject.tinyiptvkmp.core.foundation.model

enum class VideoSize {
    Cinematic,
    FitScreen,
    WideScreen,
    FillScreen,
    FullScreen;

    companion object {
        fun toggleVideoSize(current: VideoSize) =
            when (current) {
                Cinematic -> FitScreen
                FitScreen -> WideScreen
                WideScreen -> FillScreen
                FillScreen -> FullScreen
                FullScreen -> Cinematic
            }
    }
}
