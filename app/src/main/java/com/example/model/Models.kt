package com.example.model

data class Post(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatar: String,
    val isVerified: Boolean = false,
    val timeAgo: String,
    val content: String,
    val tags: List<String> = emptyList(),
    val mediaUrl: String? = null,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val category: String, // "Gaming", "Entertainment", "Tech", "Lifestyle", "Art"
    val reactions: Map<String, Int> = mapOf("❤️" to 24, "🔥" to 15, "🚀" to 9, "👏" to 5),
    val userReaction: String? = null
)

data class Story(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatar: String,
    val isViewed: Boolean = false,
    val hasUnseen: Boolean = false,
    val isUserStory: Boolean = false,
    val timeAgo: String = "2h ago",
    val title: String = ""
)

data class Reel(
    val id: String,
    val creatorName: String,
    val creatorHandle: String,
    val creatorAvatar: String,
    val isFollowed: Boolean = false,
    val caption: String,
    val tags: List<String> = emptyList(),
    val soundTrackTitle: String,
    val soundArtist: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val category: String,
    val hookSummary: String = "0-3s high-velocity curiosity trigger",
    val viralityScore: Int = 94,
    val mediaColorHex: Long = 0xFF14202E
)

data class LongVideo(
    val id: String,
    val title: String,
    val channelName: String,
    val channelAvatar: String,
    val views: String,
    val timeAgo: String,
    val duration: String,
    val category: String,
    val description: String,
    val isLive: Boolean = false,
    val hookAnalysisAvailable: Boolean = true,
    val viralityScore: Int = 91,
    val videoPreviewColor: Long = 0xFF121B27,
    val thumbnailUrl: String = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800",
    val chapters: List<String> = listOf("00:00 Intro", "03:15 The Hook", "12:40 Revelation", "24:10 Conclusion")
)

data class ExtractedHook(
    val id: String,
    val timestampStart: String,
    val timestampEnd: String,
    val title: String,
    val viralityReason: String,
    val transcriptSnippet: String,
    val retentionBoost: String, // e.g. "+38% retention"
    val viralityScore: Int // 0-100
)

data class HookAnalysisResult(
    val videoId: String,
    val videoTitle: String,
    val overallScore: Int,
    val analyzedDuration: String,
    val hooks: List<ExtractedHook>,
    val suggestedCaption: String,
    val targetPlatform: String = "Reels & TikTok 9:16"
)

data class CreatorProfile(
    val id: String = "user_me",
    val name: String = "Alex Vance",
    val handle: String = "@alexvance_fx",
    val bio: String = "🎬 Viral Hook Strategist & Video Architect | 40M+ Organic Views | Open for Collabs & Commercial Edits 🚀",
    val avatar: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
    val banner: String = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=900",
    val postsCount: Int = 138,
    val followersCount: String = "463.5K",
    val followingCount: String = "321",
    val rating: Double = 4.98,
    val reviewsCount: Int = 89,
    val isVerified: Boolean = true,
    val totalEarnings: Double = 12495.00,
    val pendingPayout: Double = 1420.50,
    val stripeConnected: Boolean = true,
    val stripeAccountId: String = "acct_1NZ4209xStripe",
    val razorpayConnected: Boolean = true,
    val razorpayKeyId: String = "rzp_live_k7h298sX",
    val upiConnected: Boolean = true,
    val upiId: String = "alexvance@okaxis",
    val recentPayouts: List<PayoutRecord> = emptyList(),
    val services: List<FreelanceService> = emptyList(),
    val publishedReels: List<Reel> = emptyList(),
    val longFormProjects: List<LongVideo> = emptyList()
)

data class FreelanceService(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val turnaround: String,
    val tags: List<String>,
    val rating: Double = 5.0,
    val completedOrders: Int = 34
)

data class PayoutRecord(
    val id: String,
    val date: String,
    val amount: Double,
    val method: String, // "Stripe", "Razorpay", "UPI"
    val status: String // "Completed", "Processing"
)

data class Milestone(
    val id: String,
    val title: String,
    val amount: Double,
    val percentage: Int,
    val isCompleted: Boolean = false,
    val isFunded: Boolean = false
)

data class HireOrder(
    val id: String,
    val creatorName: String,
    val creatorHandle: String,
    val serviceTitle: String,
    val totalAmount: Double,
    val milestones: List<Milestone>,
    val status: String, // "Escrow Funded", "In Progress", "Completed"
    val creationDate: String,
    val projectBrief: String,
    val paymentMethod: String
)

data class JobOpening(
    val id: String,
    val title: String,
    val department: String, // "Creative Studio", "AI Lab", "Audio Eng"
    val location: String, // "Remote (Global)", "Hybrid SF"
    val type: String, // "Full-time", "Contract Freelance"
    val compensation: String,
    val description: String,
    val requirements: List<String>,
    val isUrgent: Boolean = false
)

data class JobApplication(
    val id: String,
    val jobTitle: String,
    val candidateName: String,
    val candidateEmail: String,
    val portfolioUrl: String,
    val pitch: String,
    val expectedRate: String,
    val status: String = "Scout Review In Progress"
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val isMe: Boolean,
    val text: String,
    val timestamp: String,
    val isVoiceNote: Boolean = false,
    val voiceDuration: String = "0:14",
    val isRead: Boolean = true
)

data class ChatConversation(
    val id: String,
    val peerName: String,
    val peerHandle: String,
    val peerAvatar: String,
    val isOnline: Boolean,
    val lastMessage: String,
    val lastTimestamp: String,
    val unreadCount: Int = 0,
    val isEncrypted: Boolean = true,
    val messages: List<ChatMessage> = emptyList()
)

data class Category(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val count: String
)

data class SubscriptionTier(
    val id: String,
    val name: String,
    val priceMonthly: Double,
    val priceYearly: Double,
    val badgeEmoji: String,
    val badgeColorHex: Long,
    val tagLine: String,
    val features: List<String>,
    val isPopular: Boolean = false,
    val isCurrentTier: Boolean = false
)

enum class VideoAiToolType(val title: String, val iconEmoji: String, val description: String) {
    UPSCALE_4K("4K Neural Upscaler", "✨", "Super-resolution 4K HDR boost with neural denoising"),
    KINETIC_SUBTITLES("Kinetic AI Subtitles", "💬", "Dynamic word-by-word animated captions with sound FX"),
    VOCAL_ISOLATION("Studio Vocal Cleaner", "🎙️", "Remove background noise & boost audio fidelity to 96kHz"),
    SCRIPT_DOCTOR("Gemini Script Doctor", "🧠", "Analyze retention drop-off & optimize viral 3s hooks")
}

data class VideoEnhancementJob(
    val id: String,
    val toolType: VideoAiToolType,
    val videoTitle: String,
    val status: String, // "Queued", "Processing", "Completed"
    val progress: Float = 0f,
    val resultPreviewUrl: String? = null,
    val metadataSummary: String = "",
    val timestamp: String = "Just now"
)

data class GeminiScriptOptimization(
    val viralScore: Int,
    val retentionDropRisk: String, // e.g. "Low (8% drop risk)"
    val hookVariations: List<String>,
    val pacingPointers: List<String>,
    val recommendedAudienceReaction: String
)

