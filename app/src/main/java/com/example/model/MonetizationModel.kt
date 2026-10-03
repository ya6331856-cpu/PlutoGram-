package com.example.model

/**
 * Smart Creator Monetization System
 * Combining YouTube-style RPM/CPM ad revenue share, Telegram Stars micro-tipping,
 * Channel Memberships, and Escrow Collabs.
 */
data class CreatorMonetizationState(
    val isPartnerVerified: Boolean = true,
    val partnerTier: String = "Plutogram Gold Partner",
    val totalLifetimeEarnings: Double = 14850.75,
    val availableBalance: Double = 2340.50,
    val monthlyRevenue: Double = 3420.00,
    val averageRpm: Double = 4.85, // Revenue per 1,000 views in USD
    val monetizedViewsCount: Long = 1845000L,
    val watchTimeHours: Long = 48200L,
    val activeMembershipsCount: Int = 142,
    val membershipMonthlyFee: Double = 4.99,
    val starsReceived: Long = 48500L,
    val defaultAdSplitRatio: Float = 0.70f, // 70% to creator, 30% platform
    val isMonetizationEnabledGlobal: Boolean = true,
    val partnerMilestones: PartnerEligibility = PartnerEligibility(),
    val revenueBreakdown: RevenueBreakdown = RevenueBreakdown(),
    val videoEarningsList: List<VideoMonetizationItem> = emptyList(),
    val payoutHistory: List<PayoutRecord> = emptyList()
)

data class PartnerEligibility(
    val currentFollowers: Int = 463500,
    val requiredFollowers: Int = 1000,
    val currentWatchHours: Int = 48200,
    val requiredWatchHours: Int = 4000,
    val communityStrikes: Int = 0,
    val twoStepVerification: Boolean = true,
    val isEligible: Boolean = true
)

data class RevenueBreakdown(
    val adRevenue: Double = 1820.00, // 53%
    val superThanksAndStars: Double = 890.50, // 26%
    val channelMemberships: Double = 510.00, // 15%
    val brandSponsorships: Double = 200.00 // 6%
)

data class VideoMonetizationItem(
    val videoId: String,
    val title: String,
    val views: String,
    val viewsCount: Long,
    val earnings: Double,
    val rpm: Double,
    val isMonetized: Boolean = true,
    val midrollAdsEnabled: Boolean = true,
    val superThanksCount: Int = 24
)

enum class PayoutGateway(
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val processingTime: String
) {
    UPI_INSTANT("Instant UPI (India)", "Google Pay / PhonePe / Paytm", "⚡", "Instant (0-5 mins)"),
    BANK_TRANSFER("Direct Bank Wire", "NEFT / RTGS / SWIFT ACH", "🏦", "1-2 business days"),
    STRIPE_CONNECT("Stripe Direct Payout", "Global Debit / Credit Card", "💳", "Instant to 24 hrs"),
    TONCOIN_WALLET("Telegram TON Wallet", "Crypto Web3 Decentralized", "💎", "Instant (Blockchain)")
}

data class SuperThanksGiftTier(
    val id: String,
    val stars: Int,
    val usdAmount: Double,
    val iconEmoji: String,
    val label: String,
    val badgeColorHex: Long
)
