# Tiny Iptv Player

The Iptv multiplatform video player, supporting m3u/m3u8 playlists, providing epg info for available
iptv
channels, support gestures for switching volume/channels, scheduling updates period
available on Android and Windows Desktop

## Features

* Support m3u/m3u8 playlists local and remote
* Epg provider
* Horizontal swipe to change current playing channels
* Vertical swipe to change volume
* Resize (Fit, Fill, Crop)
* Ratio (WideScreen, FullScreen, Cinematic)
* Scheduling updates for channels and epg
* Overlay for channel selection, epg and program info

## Common

### UI

#### [Jetpack Compose](https://developer.android.com/jetpack/compose)

Tiny Iptv Player is fully written with Compose, using AndroidView in some cases

### Networking

#### [Ktor](https://ktor.io/)

Tiny Iptv Player use for requests for playlist and epg data and parse responses

### Navigation

#### [Voyager](https://voyager.adriel.cafe/)
A multiplatform navigation library built for Compose

### Database

#### [SQLDelight](https://cashapp.github.io/sqldelight/2.0.0/)
SQLDelight generates typesafe kotlin APIs from your SQL statements. It verifies your schema,
statements, and migrations at compile-time and provides IDE features like autocomplete and
refactoring which make writing and maintaining SQL simple.

### DI

#### [Koin](https://insert-koin.io/)

The pragmatic Kotlin & Kotlin Multiplatform Dependency Injection framework

### Theming

#### [Material 3 Theming](https://m3.material.io/develop/android/mdc-android)

Tiny Iptv Kmp is using brand new Material 3.
[Material Theme builder](https://material-foundation.github.io/material-theme-builder/#/custom) is
used to create material 3 theme and directly export it for Compose.

## Android

Player based on [Media3](https://github.com/androidx/media)
It uses ExoPlayer's ``ffmpeg`` extension
with [all its audio formats](https://exoplayer.dev/supported-formats.html#ffmpeg-extension)
enabled (it can handle even special formats like AC3, EAC3, DTS, DTS HD, TrueHD etc.).

## Screenshots

<img src="screenshots/tiny_iptv_player_1.png" height="680" width="340">
<img src="screenshots/tiny_iptv_player_2.png" height="680" width="340">
<img src="screenshots/tiny_iptv_player_3.png" height="680" width="340">
<img src="screenshots/tiny_iptv_player_4.png" height="680" width="340">
<img src="screenshots/tiny_iptv_player_5.png" height="680" width="340">

## Desktop (Windows)

Player based on [Vlc](https://github.com/caprica/vlcj)
port for java
as it
use [SwingPanel](https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Swing_Integration)
for display, so some features currently not work proper, such as overlay, swipes

<img src="screenshots/tiny_iptv_player_desktop_1.png" height="680" width="940">
<img src="screenshots/tiny_iptv_player_desktop_2.png" height="680" width="940">
<img src="screenshots/tiny_iptv_player_desktop_3.png" height="680" width="940">
<img src="screenshots/tiny_iptv_player_desktop_4.png" height="680" width="940">









