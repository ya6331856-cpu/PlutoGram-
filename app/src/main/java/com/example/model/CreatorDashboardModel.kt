package com.example.model

/**
 * Data structures for the Creator Dashboard & Studio Analytics
 * Providing YouTube Studio-grade metrics, audience demographics,
 * real-time activity tracking, and community management.
 */
data class CreatorDashboardData(
    val channelOverview: ChannelOverviewMetrics = ChannelOverviewMetrics(),
    val latestUploadPerformance: LatestUploadPerformance = LatestUploadPerformance(),
    val realTimeActivity: RealTimeActivity = RealTimeActivity(),
    val audienceInsights: AudienceInsights = AudienceInsights(),
    val priorityComments: List<CreatorPriorityComment> = emptyList(),
    val contentHealthStatus: ContentHealthStatus = ContentHealthStatus()
)

data class ChannelOverviewMetrics(
    val subscribersCount: Long = 463520L,
    val subscribersGrowth28d: Int = 1240,
    val totalViews28d: Long = 2410500L,
    val viewsGrowthPct: Float = 18.4f,
    val watchTimeHours28d: Long = 142800L,
    val watchTimeGrowthPct: Float = 22.1f,
    val estimatedRevenue28d: Double = 3420.00,
    val revenueGrowthPct: Float = 15.2f,
    val averageRpm: Double = 4.85
)

data class LatestUploadPerformance(
    val videoId: String = "v1",
    val title: String = "Cinematic Lighting Masterclass: 3-Point Light in Unreal 5.4",
    val thumbnail: String = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=800",
    val uploadTimeAgo: String = "Uploaded 18 hours ago",
    val rankingAmongLast10: Int = 1,
    val views: Long = 48250L,
    val typicalViewsRange: String = "12,000 - 28,000",
    val impressionsCtrPct: Float = 11.8f,
    val averageViewDuration: String = "4m 32s",
    val averagePercentageViewed: Float = 68.4f,
    val likesCount: Int = 3840,
    val commentsCount: Int = 412
)

data class RealTimeActivity(
    val viewsLast48Hours: Long = 184500L,
    val viewsLast60Minutes: Long = 4280L,
    val hourlyDataPoints: List<HourlyViewPoint> = listOf(
        HourlyViewPoint("12h", 2100),
        HourlyViewPoint("10h", 2450),
        HourlyViewPoint("8h", 3100),
        HourlyViewPoint("6h", 3900),
        HourlyViewPoint("4h", 4800),
        HourlyViewPoint("2h", 4200),
        HourlyViewPoint("Now", 4280)
    ),
    val topRealTimeVideos: List<TopRealTimeVideo> = listOf(
        TopRealTimeVideo("Unreal 5.4 Cinematic Lighting", "1,820/hr", "+24%"),
        TopRealTimeVideo("1v5 Clutch Sound Anticipation", "1,410/hr", "+18%"),
        TopRealTimeVideo("Sub-Bass Impacts Sound Design", "740/hr", "+8%")
    )
)

data class HourlyViewPoint(
    val timeLabel: String,
    val viewCount: Int
)

data class TopRealTimeVideo(
    val title: String,
    val velocity: String,
    val delta: String
)

data class AudienceInsights(
    val returningViewersPct: Float = 62.0f,
    val newViewersPct: Float = 38.0f,
    val topGeographies: List<GeographyShare> = listOf(
        GeographyShare("India 🇮🇳", 44f),
        GeographyShare("United States 🇺🇸", 26f),
        GeographyShare("Germany 🇩🇪", 11f),
        GeographyShare("United Kingdom 🇬🇧", 9f),
        GeographyShare("Others 🌐", 10f)
    ),
    val ageDemographics: List<DemographicSegment> = listOf(
        DemographicSegment("18-24 yrs", 48f),
        DemographicSegment("25-34 yrs", 36f),
        DemographicSegment("35-44 yrs", 12f),
        DemographicSegment("45+ yrs", 4f)
    )
)

data class GeographyShare(
    val country: String,
    val percentage: Float
)

data class DemographicSegment(
    val segment: String,
    val percentage: Float
)

data class CreatorPriorityComment(
    val id: String,
    val author: String,
    val authorAvatar: String,
    val timeAgo: String,
    val content: String,
    val videoTitle: String,
    val isSuperThanks: Boolean = false,
    val starsTipped: Int = 0,
    val isHeartedByCreator: Boolean = false,
    val creatorReply: String? = null
)

data class ContentHealthStatus(
    val copyrightClaimsCount: Int = 0,
    val communityStrikesCount: Int = 0,
    val contentIdProtectionActive: Boolean = true,
    val isTermsCompliant: Boolean = true
)
