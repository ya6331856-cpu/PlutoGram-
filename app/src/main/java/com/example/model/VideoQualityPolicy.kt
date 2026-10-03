package com.example.model

enum class VideoQualityPreset(
    val label: String,
    val resolution: String,
    val bitrate: String,
    val minSpeedMbps: Float,
    val badge: String
) {
    FULL_HD_1080P(
        label = "1080p Full HD (Display Native)",
        resolution = "1920x1080 @ 60fps",
        bitrate = "12 Mbps Lossless",
        minSpeedMbps = 8.0f,
        badge = "1080p 60fps"
    ),
    ULTRA_HD_4K(
        label = "4K Cinema Ultra HDR",
        resolution = "3840x2160 @ 60fps HDR",
        bitrate = "28 Mbps Master",
        minSpeedMbps = 25.0f,
        badge = "4K HDR"
    ),
    DISPLAY_MAX(
        label = "Device Screen Max (120Hz)",
        resolution = "Display Native OLED / AMOLED",
        bitrate = "18 Mbps Direct",
        minSpeedMbps = 15.0f,
        badge = "PRO MAX"
    )
}

enum class NetworkBandwidthState(
    val title: String,
    val speedMbps: Float,
    val description: String
) {
    HIGH_SPEED_5G(
        title = "High Speed (5G / Fiber)",
        speedMbps = 45.0f,
        description = "Sufficient for 1080p/4K 60fps streaming"
    ),
    STABLE_WIFI(
        title = "Standard Wi-Fi",
        speedMbps = 16.0f,
        description = "Optimal for 1080p Full HD streaming"
    ),
    SLOW_NETWORK(
        title = "Slow / Congested (3G)",
        speedMbps = 1.8f,
        description = "Below HD threshold. Triggers quality protection pause."
    )
}
