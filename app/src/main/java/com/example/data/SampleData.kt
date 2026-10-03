package com.example.data

import com.example.model.*

object SampleData {

    val categories = listOf(
        Category("gaming", "Gaming", "🎮", "142K clips"),
        Category("entertainment", "Entertainment", "🎧", "380K clips"),
        Category("tech", "Tech", "💻", "95K clips"),
        Category("lifestyle", "Lifestyle", "🌿", "210K clips"),
        Category("music", "Music", "🎵", "160K clips"),
        Category("art", "Art", "🎨", "78K clips")
    )

    val stories = listOf(
        Story("s0", "Your Story", "@alexvance_fx", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300", isUserStory = true),
        Story("s1", "Elena Rostova", "@elena_cinema", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300", hasUnseen = true, timeAgo = "18m ago", title = "Color Grade"),
        Story("s2", "Marcus Kane", "@kane_fx", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300", hasUnseen = true, timeAgo = "45m ago", title = "Hook Breakdown"),
        Story("s3", "Sora Tanaka", "@soravfx", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300", hasUnseen = false, timeAgo = "2h ago", title = "Blender 4.2"),
        Story("s4", "Devon Miles", "@devon_beats", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300", hasUnseen = false, timeAgo = "4h ago", title = "Audio Master"),
        Story("s5", "Aria Chen", "@aria_art", "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=300", hasUnseen = true, timeAgo = "5h ago", title = "Tokyo Night")
    )

    val initialPosts = listOf(
        Post(
            id = "p1",
            authorName = "Elena Rostova",
            authorHandle = "@elena_cinema",
            authorAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300",
            isVerified = true,
            timeAgo = "12 min ago",
            content = "Just tested the new AI Video Hook Studio on my 45-minute cinema lighting masterclass. Extracted 4 high-retention 12s hooks in under 15 seconds. Retention jumped +42% on the first cut! 🔥🎬",
            tags = listOf("#filmmaking", "#AIStudio", "#videoediting", "#cinema"),
            mediaUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=800",
            likesCount = 342,
            commentsCount = 48,
            sharesCount = 67,
            category = "Entertainment",
            reactions = mapOf("❤️" to 142, "🔥" to 89, "🚀" to 34, "👏" to 19)
        ),
        Post(
            id = "p2",
            authorName = "GhostRider Gaming",
            authorHandle = "@ghostrider_fps",
            authorAvatar = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=300",
            isVerified = true,
            timeAgo = "34 min ago",
            content = "1v5 Clutch in the finals with 0.4 seconds left on the defuse! Prompted the AI Studio with 'Find extreme tension turnaround' and it clipped the exact heart-rate spike waveform. Clip is now in Reels! 🎮💥",
            tags = listOf("#gaming", "#valorant", "#esports", "#clutch"),
            mediaUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800",
            likesCount = 890,
            commentsCount = 112,
            sharesCount = 204,
            category = "Gaming",
            reactions = mapOf("🔥" to 420, "🚀" to 198, "❤️" to 154, "🤯" to 85)
        ),
        Post(
            id = "p3",
            authorName = "Kai Tech Lab",
            authorHandle = "@kaitechtalks",
            authorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=300",
            isVerified = true,
            timeAgo = "2 hours ago",
            content = "Paid out $1,850 direct to freelance hook editors through the profile Stripe/UPI gateway today. Fast escrow milestone release after reviewing the 4K render revisions. This workflow is game changing for creators! ⚡️💳",
            tags = listOf("#freelance", "#creatorEconomy", "#tech", "#stripe"),
            mediaUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800",
            likesCount = 612,
            commentsCount = 76,
            sharesCount = 92,
            category = "Tech",
            reactions = mapOf("🚀" to 280, "👏" to 175, "❤️" to 98, "💡" to 62)
        ),
        Post(
            id = "p4",
            authorName = "Maya Botanical",
            authorHandle = "@maya_nordic",
            authorAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300",
            isVerified = false,
            timeAgo = "4 hours ago",
            content = "Morning routine in Kyoto studio. When the minimalist aesthetic matches the calm mind, creative video flow follows effortlessly. What is your go-to soundtrack when editing? 🍵✨",
            tags = listOf("#lifestyle", "#kyoto", "#nordic", "#productivity"),
            mediaUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800",
            likesCount = 245,
            commentsCount = 31,
            sharesCount = 19,
            category = "Lifestyle",
            reactions = mapOf("❤️" to 160, "🌿" to 54, "✨" to 32)
        )
    )

    val initialReels = listOf(
        Reel(
            id = "r1",
            creatorName = "Alex Vance",
            creatorHandle = "@alexvance_fx",
            creatorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
            isFollowed = true,
            caption = "The exact 3-second hook framework that brought 12M views on long-form repurposing. Save this for your next video! 🚀⚡️",
            tags = listOf("#hookstrategy", "#videocreator", "#algorithm", "#viralreels"),
            soundTrackTitle = "Obsidian Waves (Original Mix)",
            soundArtist = "Plutogram Studio Audio",
            likesCount = 28410,
            commentsCount = 620,
            sharesCount = 4130,
            category = "Tech",
            hookSummary = "Pattern Interrupt + Negative Curiosity framing",
            viralityScore = 98,
            mediaColorHex = 0xFF142436
        ),
        Reel(
            id = "r2",
            creatorName = "Apex Valkyrie",
            creatorHandle = "@valk_fps",
            creatorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
            isFollowed = false,
            caption = "They thought the round was lost at 1 HP... never give up on the site defense! 🎮🔥",
            tags = listOf("#gaming", "#apex", "#insaneclutch", "#highlight"),
            soundTrackTitle = "HyperDrive Bass Boost",
            soundArtist = "CyberGamer Sounds",
            likesCount = 45200,
            commentsCount = 1140,
            sharesCount = 8900,
            category = "Gaming",
            hookSummary = "Instant gunshot audio sync + Heartbeat buildup",
            viralityScore = 95,
            mediaColorHex = 0xFF1E1C2E
        ),
        Reel(
            id = "r3",
            creatorName = "Marcus Kane",
            creatorHandle = "@kane_fx",
            creatorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300",
            isFollowed = false,
            caption = "Color grading LUTs don't fix bad lighting. Here is the 1 single softbox setup that makes any camera look $10,000 🎬💡",
            tags = listOf("#cinematography", "#lighting", "#filmmaker", "#gear"),
            soundTrackTitle = "Midnight Echoes",
            soundArtist = "LoFi Soundscapes",
            likesCount = 19300,
            commentsCount = 410,
            sharesCount = 3200,
            category = "Entertainment",
            hookSummary = "Myth Debunk Hook + Dramatic Split Screen",
            viralityScore = 93,
            mediaColorHex = 0xFF162520
        ),
        Reel(
            id = "r4",
            creatorName = "Aria Chen",
            creatorHandle = "@aria_art",
            creatorAvatar = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=300",
            isFollowed = true,
            caption = "140 hours in Unreal Engine 5 compressed into 30 seconds of pure cyberpunk Tokyo vibe 🎨✨",
            tags = listOf("#digitalart", "#unrealengine", "#3danimation", "#cyberpunk"),
            soundTrackTitle = "Neo Shibuya Synth",
            soundArtist = "RetroWave Lab",
            likesCount = 63400,
            commentsCount = 1890,
            sharesCount = 12400,
            category = "Art",
            hookSummary = "Fast visual crescendo with sound design drop",
            viralityScore = 97,
            mediaColorHex = 0xFF2D1629
        )
    )

    val initialLongVideos = listOf(
        LongVideo(
            id = "lv1",
            title = "Building a $100K/Mo Creator Agency: AI Video Hook Workflows & Client Retention",
            channelName = "Alex Vance Studio",
            channelAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
            views = "420K views",
            timeAgo = "2 days ago",
            duration = "24:18",
            category = "Tech",
            description = "Complete masterclass on how we dissect 1-hour YouTube podcasts into 25 high-performing vertical shorts using AI Studio hook extraction, waveform cadence analysis, and direct freelance editor payouts.",
            isLive = false,
            viralityScore = 96,
            videoPreviewColor = 0xFF132030
        ),
        LongVideo(
            id = "lv2",
            title = "LIVE: Global Cyber Championship Finals - Grand Arena Stage 4",
            channelName = "Apex Esports TV",
            channelAvatar = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=300",
            views = "14.2K watching",
            timeAgo = "Streaming now",
            duration = "LIVE",
            category = "Gaming",
            description = "Top 8 international finalists battle in double elimination best of 5. Real-time hook studio highlights automatically clipped as clutches happen.",
            isLive = true,
            viralityScore = 98,
            videoPreviewColor = 0xFF20162B
        ),
        LongVideo(
            id = "lv3",
            title = "Hollywood Director Breaks Down 7 Iconic Scene Openers & Why They Hooked The World",
            channelName = "CinemaCraft Insights",
            channelAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300",
            views = "1.2M views",
            timeAgo = "1 week ago",
            duration = "38:45",
            category = "Entertainment",
            description = "A shot-by-shot psychological deconstruction of opening camera angles, auditory hooks, and pacing secrets used by Christopher Nolan and David Fincher.",
            isLive = false,
            viralityScore = 92,
            videoPreviewColor = 0xFF172421
        ),
        LongVideo(
            id = "lv4",
            title = "Minimalist Workspaces: High-Performance Studio Setup for Video Editors & 3D Artists",
            channelName = "Nordic Aesthetic Design",
            channelAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300",
            views = "280K views",
            timeAgo = "3 days ago",
            duration = "16:04",
            category = "Lifestyle",
            description = "Ergonomics, acoustic panels, custom frosted desk dividers, dual reference monitors, and clean cable routing designed for long editing sessions.",
            isLive = false,
            viralityScore = 89,
            videoPreviewColor = 0xFF1F2226
        )
    )

    val sampleHooks = listOf(
        ExtractedHook(
            id = "h1",
            timestampStart = "01:24",
            timestampEnd = "01:42",
            title = "The Secret Weapon Framework",
            viralityReason = "Unexpected statement challenging consensus with instant visual proof",
            transcriptSnippet = "\"Most editors spend 6 hours on color grade, but watch what happens when you flip the first 3 seconds upside down...\"",
            retentionBoost = "+48% Avg Watchtime",
            viralityScore = 97
        ),
        ExtractedHook(
            id = "h2",
            timestampStart = "07:15",
            timestampEnd = "07:33",
            title = "The High-Stakes Turnaround",
            viralityReason = "Intense audio pitch shift & dramatic cliffhanger question",
            transcriptSnippet = "\"We were about to lose the $15,000 client until our prompt engineer found this exact timeline gap...\"",
            retentionBoost = "+39% Completion Rate",
            viralityScore = 94
        ),
        ExtractedHook(
            id = "h3",
            timestampStart = "14:50",
            timestampEnd = "15:08",
            title = "The Live Demo Punchline",
            viralityReason = "Clear before/after transformation with satisfying result",
            transcriptSnippet = "\"Before: 200 views. After this one waveform trim: 1.8 Million views in 48 hours. Here is the exact metric.\"",
            retentionBoost = "+52% Share Rate",
            viralityScore = 99
        )
    )

    val freelanceServices = listOf(
        FreelanceService(
            id = "serv1",
            title = "Viral Short-Form Hook Extractor & 9:16 Re-cutter",
            description = "I dissect your 30-60 min long video, podcast, or gaming stream into 3 viral high-retention vertical reels with dynamic subtitles, sound design & B-roll.",
            price = 180.00,
            turnaround = "24-48 Hours",
            tags = listOf("AI Hooks", "Reels/TikTok", "Sound FX", "Color Grade"),
            rating = 5.0,
            completedOrders = 64
        ),
        FreelanceService(
            id = "serv2",
            title = "Cinema Grade Commercial Intro / Trailer Hook (15s)",
            description = "Custom 3D typography, audio design master, pacing synchronization tailored for advertising campaigns and product unveilings.",
            price = 350.00,
            turnaround = "3 Days",
            tags = listOf("After Effects", "3D Motion", "4K HDR", "Commercial"),
            rating = 4.9,
            completedOrders = 38
        ),
        FreelanceService(
            id = "serv3",
            title = "Full Channel Video Retention Audit & Strategy Consultation",
            description = "Deep analytics review of your drop-off timestamps, thumbnail CTR, and custom prompt templates for the AI Hook Studio.",
            price = 220.00,
            turnaround = "48 Hours",
            tags = listOf("Auditing", "Hook Strategy", "1-on-1 Call"),
            rating = 5.0,
            completedOrders = 29
        )
    )

    val jobOpenings = listOf(
        JobOpening(
            id = "job1",
            title = "Senior Video Architect & AI Hook Director",
            department = "Creative Studio",
            location = "Remote (Worldwide)",
            type = "Full-time / High Retainer",
            compensation = "$85K - $120K / yr + Equity",
            description = "Lead creative direction for top creator partners, train custom Gemini prompt flows for viral hook discovery, and oversee 9:16 post-production workflows.",
            requirements = listOf(
                "3+ years in high-velocity YouTube/TikTok post production",
                "Proven portfolio of 10M+ aggregate organic views",
                "Experience with AI prompt engineering & audio waveform editing",
                "Mastery of Premiere Pro, DaVinci Resolve, or Final Cut"
            ),
            isUrgent = true
        ),
        JobOpening(
            id = "job2",
            title = "Sound Designer & Kinetic Subtitle Specialist",
            department = "Audio Lab",
            location = "Remote (Worldwide)",
            type = "Contract Freelance",
            compensation = "$40 - $70 / hour",
            description = "Craft immersive micro-audio soundscapes, whooshes, riser impacts, and stylized kinetic typography for high-retention vertical clips.",
            requirements = listOf(
                "Deep library of commercial sound design assets",
                "Ability to deliver synchronized captions within 12h turnaround",
                "Collaborative workflow via Telegram encrypted client channels"
            ),
            isUrgent = false
        ),
        JobOpening(
            id = "job3",
            title = "Gaming Clip Scout & Esports Narrative Editor",
            department = "Gaming Division",
            location = "Remote (Worldwide)",
            type = "Flexible Contract",
            compensation = "$3,000 - $5,500 / month",
            description = "Monitor live tournament streams, identify clutch moments, and package viral narratives for competitive FPS and MOBA communities.",
            requirements = listOf(
                "Deep knowledge of Valorant, CS2, Apex Legends, League",
                "Instant turnaround during major weekend tournaments",
                "Strong sense of comedic timing and gamer culture"
            ),
            isUrgent = true
        )
    )

    val initialConversations = listOf(
        ChatConversation(
            id = "c1",
            peerName = "David Keller (Creative Producer)",
            peerHandle = "@david_keller",
            peerAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300",
            isOnline = true,
            lastMessage = "Approved milestone 1! The $180 is deposited into your escrow balance. Ready for Milestone 2?",
            lastTimestamp = "14:22",
            unreadCount = 2,
            isEncrypted = true,
            messages = listOf(
                ChatMessage("m1", "David Keller", false, "Hey Alex! Loved your recent portfolio reel on the hook breakdown.", "13:50"),
                ChatMessage("m2", "Alex Vance", true, "Thanks David! That clip was generated with our AI Hook Studio.", "14:02"),
                ChatMessage("m3", "David Keller", false, "We have a 40-minute tech talk that needs 3 killer hooks for LinkedIn & YouTube Shorts.", "14:10"),
                ChatMessage("m4", "Alex Vance", true, "Perfect. I sent over the freelance contract offer with 50% escrow deposit.", "14:18"),
                ChatMessage("m5", "David Keller", false, "Approved milestone 1! The $180 is deposited into your escrow balance. Ready for Milestone 2?", "14:22")
            )
        ),
        ChatConversation(
            id = "c2",
            peerName = "Elena Rostova",
            peerHandle = "@elena_cinema",
            peerAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300",
            isOnline = true,
            lastMessage = "Voice note: (0:24) Loved the sound design transition at 01:34!",
            lastTimestamp = "Yesterday",
            unreadCount = 0,
            isEncrypted = true,
            messages = listOf(
                ChatMessage("m1", "Elena Rostova", false, "Check out the color profile I exported for your camera", "Yesterday"),
                ChatMessage("m2", "Alex Vance", true, "Imported it into the studio timeline. Looks ultra clean in dark mode.", "Yesterday"),
                ChatMessage("m3", "Elena Rostova", false, "Voice note: (0:24) Loved the sound design transition at 01:34!", "Yesterday", isVoiceNote = true, voiceDuration = "0:24")
            )
        ),
        ChatConversation(
            id = "c3",
            peerName = "Plutogram Talent Scout",
            peerHandle = "@plutogram_careers",
            peerAvatar = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=300",
            isOnline = false,
            lastMessage = "Your career application for 'Senior Video Architect' has been received. Our lead scout will review it within 24h.",
            lastTimestamp = "Sep 29",
            unreadCount = 0,
            isEncrypted = true,
            messages = listOf(
                ChatMessage("m1", "Plutogram Talent Scout", false, "Your career application for 'Senior Video Architect' has been received. Our lead scout will review it within 24h.", "Sep 29")
            )
        )
    )

    val initialPayouts = listOf(
        PayoutRecord("p1", "Oct 01, 2026", 450.00, "Stripe Connect", "Completed"),
        PayoutRecord("p2", "Sep 28, 2026", 720.00, "UPI (alexvance@okaxis)", "Completed"),
        PayoutRecord("p3", "Sep 22, 2026", 1150.00, "Razorpay Express", "Completed"),
        PayoutRecord("p4", "Sep 15, 2026", 320.00, "Stripe Connect", "Completed")
    )

    val superThanksGiftTiers = listOf(
        SuperThanksGiftTier("gift_50", 50, 1.00, "⭐", "50 Stars (Heart Spark)", 0xFFFFD700),
        SuperThanksGiftTier("gift_250", 250, 5.00, "🚀", "250 Stars (Rocket Boost)", 0xFF00E5FF),
        SuperThanksGiftTier("gift_1000", 1000, 20.00, "🔥", "1,000 Stars (Viral Flame)", 0xFFFF5722),
        SuperThanksGiftTier("gift_5000", 5000, 100.00, "👑", "5,000 Stars (Creator Crown)", 0xFFAB47BC)
    )

    val initialMonetizationState = CreatorMonetizationState(
        isPartnerVerified = true,
        partnerTier = "Plutogram Gold Partner",
        totalLifetimeEarnings = 14850.75,
        availableBalance = 2340.50,
        monthlyRevenue = 3420.00,
        averageRpm = 4.85,
        monetizedViewsCount = 1845000L,
        watchTimeHours = 48200L,
        activeMembershipsCount = 142,
        membershipMonthlyFee = 4.99,
        starsReceived = 48500L,
        defaultAdSplitRatio = 0.70f,
        isMonetizationEnabledGlobal = true,
        partnerMilestones = PartnerEligibility(
            currentFollowers = 463500,
            requiredFollowers = 1000,
            currentWatchHours = 48200,
            requiredWatchHours = 4000,
            communityStrikes = 0,
            twoStepVerification = true,
            isEligible = true
        ),
        revenueBreakdown = RevenueBreakdown(
            adRevenue = 1820.00,
            superThanksAndStars = 890.50,
            channelMemberships = 510.00,
            brandSponsorships = 200.00
        ),
        videoEarningsList = listOf(
            VideoMonetizationItem(
                videoId = "v1",
                title = "Cinematic Lighting Masterclass: 3-Point Light in Unreal 5.4",
                views = "480K views",
                viewsCount = 480000L,
                earnings = 840.50,
                rpm = 5.20,
                isMonetized = true,
                midrollAdsEnabled = true,
                superThanksCount = 68
            ),
            VideoMonetizationItem(
                videoId = "v2",
                title = "Sound Design Secrets: Crafting Sub-Bass Impacts That Rattle Phones",
                views = "210K views",
                viewsCount = 210000L,
                earnings = 412.00,
                rpm = 4.65,
                isMonetized = true,
                midrollAdsEnabled = true,
                superThanksCount = 32
            ),
            VideoMonetizationItem(
                videoId = "v3",
                title = "1v5 Valorant Clutch Breakdown: Frame by Frame Sound Cue Anticipation",
                views = "890K views",
                viewsCount = 890000L,
                earnings = 1320.25,
                rpm = 4.90,
                isMonetized = true,
                midrollAdsEnabled = true,
                superThanksCount = 114
            ),
            VideoMonetizationItem(
                videoId = "v4",
                title = "Tokyo Rain VFX: Particle Collision & Wet Road Reflections in Blender",
                views = "140K views",
                viewsCount = 140000L,
                earnings = 285.00,
                rpm = 4.40,
                isMonetized = true,
                midrollAdsEnabled = false,
                superThanksCount = 18
            )
        ),
        payoutHistory = initialPayouts
    )

    val initialCreatorDashboardData = CreatorDashboardData(
        channelOverview = ChannelOverviewMetrics(
            subscribersCount = 463520L,
            subscribersGrowth28d = 1240,
            totalViews28d = 2410500L,
            viewsGrowthPct = 18.4f,
            watchTimeHours28d = 142800L,
            watchTimeGrowthPct = 22.1f,
            estimatedRevenue28d = 3420.00,
            revenueGrowthPct = 15.2f,
            averageRpm = 4.85
        ),
        latestUploadPerformance = LatestUploadPerformance(
            videoId = "v1",
            title = "Cinematic Lighting Masterclass: 3-Point Light in Unreal 5.4",
            thumbnail = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=800",
            uploadTimeAgo = "Uploaded 18 hours ago",
            rankingAmongLast10 = 1,
            views = 48250L,
            typicalViewsRange = "12,000 - 28,000",
            impressionsCtrPct = 11.8f,
            averageViewDuration = "4m 32s",
            averagePercentageViewed = 68.4f,
            likesCount = 3840,
            commentsCount = 412
        ),
        realTimeActivity = RealTimeActivity(),
        audienceInsights = AudienceInsights(),
        priorityComments = listOf(
            CreatorPriorityComment(
                id = "cm1",
                author = "Jordan Reed",
                authorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
                timeAgo = "2h ago",
                content = "That bounce light setup with the warm accent completely fixed my dark scenes! Best Unreal tutorial on the platform. Sent a Super Thanks! 🔥",
                videoTitle = "Cinematic Lighting Masterclass",
                isSuperThanks = true,
                starsTipped = 250,
                isHeartedByCreator = true,
                creatorReply = "Thanks Jordan! Glad the rim bounce helped!"
            ),
            CreatorPriorityComment(
                id = "cm2",
                author = "Nadia Vance",
                authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
                timeAgo = "5h ago",
                content = "Could you do a breakdown on volumetric fog rendering in Blender vs Unreal? Would love your take on render times.",
                videoTitle = "Tokyo Rain VFX",
                isSuperThanks = false,
                starsTipped = 0,
                isHeartedByCreator = false
            ),
            CreatorPriorityComment(
                id = "cm3",
                author = "Kaito FX",
                authorAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
                timeAgo = "1d ago",
                content = "The 3-second hook extraction method you showed in the AI Studio doubled our TikTok retention rate this week. Incredible tool!",
                videoTitle = "AI Hook Studio Deep Dive",
                isSuperThanks = true,
                starsTipped = 1000,
                isHeartedByCreator = true
            )
        ),
        contentHealthStatus = ContentHealthStatus(
            copyrightClaimsCount = 0,
            communityStrikesCount = 0,
            contentIdProtectionActive = true,
            isTermsCompliant = true
        )
    )

    val initialEscrowOrders = listOf(
        HireOrder(
            id = "escrow_ord_101",
            creatorName = "Alex Vance",
            creatorHandle = "@alexvance_fx",
            serviceTitle = "15-Second Viral Hook Batch for Web3 Brand",
            totalAmount = 750.00,
            milestones = listOf(
                Milestone(id = "m101_1", title = "Script & 3s Hook Generation (AI Studio)", amount = 225.0, percentage = 30, isCompleted = true, isFunded = true),
                Milestone(id = "m101_2", title = "Rough Cut & Kinetic Subtitles", amount = 300.0, percentage = 40, isCompleted = true, isFunded = true),
                Milestone(id = "m101_3", title = "Final 4K Master & Sound Design", amount = 225.0, percentage = 30, isCompleted = false, isFunded = true)
            ),
            status = "Escrow Funded (100% Locked)",
            creationDate = "Yesterday",
            projectBrief = "Produce 3 dynamic vertical hooks showcasing instant crypto micropayments for creators.",
            paymentMethod = "Stripe Connect"
        ),
        HireOrder(
            id = "escrow_ord_102",
            creatorName = "Alex Vance",
            creatorHandle = "@alexvance_fx",
            serviceTitle = "Tokyo Rain VFX Color Grade & Sound FX",
            totalAmount = 600.00,
            milestones = listOf(
                Milestone(id = "m102_1", title = "Color Grade LUTs Preview", amount = 300.0, percentage = 50, isCompleted = true, isFunded = true),
                Milestone(id = "m102_2", title = "Final Audio Stems & Delivery", amount = 300.0, percentage = 50, isCompleted = false, isFunded = true)
            ),
            status = "In Progress",
            creationDate = "3 days ago",
            projectBrief = "Atmospheric night cyberpunk rain scene color grading in DaVinci Resolve.",
            paymentMethod = "Razorpay / UPI"
        ),
        HireOrder(
            id = "escrow_ord_103",
            creatorName = "Alex Vance",
            creatorHandle = "@alexvance_fx",
            serviceTitle = "Unreal Engine 5.4 Lumen Lighting Asset Setup",
            totalAmount = 500.00,
            milestones = listOf(
                Milestone(id = "m103_1", title = "3-Point Bounce Light Scene Files", amount = 500.0, percentage = 100, isCompleted = true, isFunded = true)
            ),
            status = "Delivered (Awaiting Client Release)",
            creationDate = "Oct 1",
            projectBrief = "Custom studio lighting blueprint template for interior archviz renders.",
            paymentMethod = "TON Crypto Wallet"
        )
    )

    val initialSubscriptionTiers = listOf(
        SubscriptionTier(
            id = "tier_free",
            name = "Free Supporter",
            priceMonthly = 0.0,
            priceYearly = 0.0,
            badgeEmoji = "🌱",
            badgeColorHex = 0xFF8E9BAE,
            tagLine = "Standard access to feed & clips",
            features = listOf(
                "Standard 1080p video streams",
                "Public social feed & creator stories",
                "Community comments & reactions"
            ),
            isCurrentTier = false
        ),
        SubscriptionTier(
            id = "tier_bronze",
            name = "Bronze Fan",
            priceMonthly = 4.99,
            priceYearly = 47.90,
            badgeEmoji = "🥉",
            badgeColorHex = 0xFFCD7F32,
            tagLine = "Exclusive videos & member chat perks",
            features = listOf(
                "Supporter Loyalty Badge in Live & Comments",
                "3 Exclusive Member-only Reels/VODs per week",
                "Priority Comment Highlights on Creator Hub",
                "Telegram VIP Chat access"
            ),
            isCurrentTier = false
        ),
        SubscriptionTier(
            id = "tier_silver",
            name = "Silver VIP Creator",
            priceMonthly = 14.99,
            priceYearly = 143.90,
            badgeEmoji = "🥈",
            badgeColorHex = 0xFF00E5FF,
            tagLine = "Full Video AI Enhancements & 4K Suite",
            features = listOf(
                "Everything in Bronze Fan",
                "4K HDR Neural Upscaler (60fps)",
                "Kinetic AI Subtitles Engine (Hormozi style)",
                "Studio Vocal Cleaner (96kHz noise removal)",
                "Gemini 3.5 Flash Script Doctor & Hook Optimizer",
                "Early access to escrow freelance gigs"
            ),
            isPopular = true,
            isCurrentTier = true
        ),
        SubscriptionTier(
            id = "tier_gold",
            name = "Gold Executive Producer",
            priceMonthly = 49.99,
            priceYearly = 479.90,
            badgeEmoji = "👑",
            badgeColorHex = 0xFFFFD700,
            tagLine = "Producer credits, 1-on-1 Collabs & Unlimited AI",
            features = listOf(
                "Everything in Silver VIP",
                "$50 Monthly Escrow Collab Credit included",
                "Executive Producer credit on all long-form videos",
                "1-on-1 Monthly Video Review stream with creator",
                "Unlimited Video AI Neural processing jobs",
                "Direct Telegram Secret Chat hotline"
            ),
            isCurrentTier = false
        )
    )

    val initialEnhancementJobs = listOf(
        VideoEnhancementJob(
            id = "job_enh_1",
            toolType = VideoAiToolType.UPSCALE_4K,
            videoTitle = "Tokyo Cyberpunk VFX Master Cut",
            status = "Completed ✅",
            progress = 1.0f,
            metadataSummary = "4K HDR 60fps (Teal & Orange)",
            timestamp = "10m ago"
        ),
        VideoEnhancementJob(
            id = "job_enh_2",
            toolType = VideoAiToolType.KINETIC_SUBTITLES,
            videoTitle = "Viral Hook 3s Teaser Cut",
            status = "Completed ✅",
            progress = 1.0f,
            metadataSummary = "Hormozi Electric Cyan + Bass SFX",
            timestamp = "45m ago"
        ),
        VideoEnhancementJob(
            id = "job_enh_3",
            toolType = VideoAiToolType.VOCAL_ISOLATION,
            videoTitle = "Cinematography Masterclass Audio Stem",
            status = "Completed ✅",
            progress = 1.0f,
            metadataSummary = "Studio Condenser 96kHz (-36dB Hum)",
            timestamp = "2h ago"
        )
    )
}
