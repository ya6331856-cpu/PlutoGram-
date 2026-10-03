package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BottomNavBar
import com.example.ui.components.CreateHubDialog
import com.example.ui.components.CreatorMindsetDialog
import com.example.ui.components.CreatorTipDialog
import com.example.ui.components.SubscriptionTiersDialog
import com.example.ui.components.SuperThanksDialog
import com.example.ui.components.TopHeader
import com.example.ui.components.WithdrawalDialog
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TelegramDarkBg
import com.example.ui.theme.TelegramEmerald
import com.example.viewmodel.TelePulseViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val viewModel: TelePulseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TelePulseApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TelePulseApp(viewModel: TelePulseViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isCreateHubOpen by remember { mutableStateOf(false) }
    var isExploreOpen by remember { mutableStateOf(false) }
    var isAppMenuOpen by remember { mutableStateOf(false) }

    // BackHandler to handle custom screen transitions gracefully
    BackHandler(enabled = uiState.currentTab != 0 || isCreateHubOpen || isExploreOpen || isAppMenuOpen || uiState.isChatOpen || uiState.isCareersOpen || uiState.isCheckoutDialogOpen || uiState.isPaymentConfigOpen || uiState.isAuthDialogOpen || uiState.isTermsDialogOpen || uiState.isCallActive || uiState.isCameraDialogOpen || uiState.isSuperThanksDialogOpen || uiState.isPayoutModalOpen || uiState.isCreatorMindsetDialogOpen || uiState.isTipDialogOpen || uiState.isSubscriptionModalOpen) {
        when {
            uiState.isSubscriptionModalOpen -> viewModel.openSubscriptionModal(false)
            uiState.isTipDialogOpen -> viewModel.closeTipDialog()
            uiState.isCreatorMindsetDialogOpen -> viewModel.openCreatorMindsetDialog(false)
            uiState.isSuperThanksDialogOpen -> viewModel.closeSuperThanksDialog()
            uiState.isPayoutModalOpen -> viewModel.openPayoutModal(false)
            isAppMenuOpen -> isAppMenuOpen = false
            isCreateHubOpen -> isCreateHubOpen = false
            isExploreOpen -> isExploreOpen = false
            uiState.isCallActive -> viewModel.endCall()
            uiState.isCameraDialogOpen -> viewModel.openCameraDialog(false)
            uiState.isTermsDialogOpen -> viewModel.openTermsDialog(false)
            uiState.isAuthDialogOpen -> viewModel.openAuthDialog(false)
            uiState.isChatOpen -> viewModel.toggleChat(false)
            uiState.isCareersOpen -> viewModel.toggleCareersPortal(false)
            uiState.isCheckoutDialogOpen -> viewModel.closeHireCheckout()
            uiState.isPaymentConfigOpen -> viewModel.openPaymentConfig(false)
            uiState.currentTab != 0 -> viewModel.selectTab(0)
        }
    }

    val screenTitle = when (uiState.currentTab) {
        0 -> "Plutogram"
        1 -> "Reels"
        2 -> "AI Video Studio"
        3 -> "Long Videos"
        4 -> "Creator Profile"
        else -> "Plutogram"
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(TelegramDarkBg)
            .testTag("plutogram_scaffold"),
        containerColor = TelegramDarkBg,
        topBar = {
            if (uiState.currentTab != 1) { // Reels screen has full-bleed immersive video overlay
                TopHeader(
                    title = screenTitle,
                    unreadMessagesCount = uiState.conversations.sumOf { it.unreadCount },
                    onOpenChat = { viewModel.toggleChat(true) },
                    onOpenExplore = { isExploreOpen = true },
                    onOpenMenu = { isAppMenuOpen = true }
                )
            }
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = uiState.currentTab,
                onTabSelected = { viewModel.selectTab(it) },
                onCenterPlusClick = { isCreateHubOpen = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = if (uiState.currentTab == 1) 0.dp else innerPadding.calculateTopPadding(),
                    bottom = 0.dp
                )
        ) {
            AnimatedContent(
                targetState = uiState.currentTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "tab_navigation"
            ) { tab ->
                when (tab) {
                    0 -> HomeScreen(uiState = uiState, viewModel = viewModel)
                    1 -> ReelsScreen(uiState = uiState, viewModel = viewModel)
                    2 -> VideoStudioScreen(uiState = uiState, viewModel = viewModel)
                    3 -> LongVideoScreen(uiState = uiState, viewModel = viewModel)
                    4 -> ProfileScreen(uiState = uiState, viewModel = viewModel)
                }
            }
        }
    }

    // Modal Overlays
    if (uiState.isChatOpen) {
        ChatScreen(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { viewModel.toggleChat(false) }
        )
    }

    if (uiState.isCareersOpen) {
        CareersPortalDialog(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { viewModel.toggleCareersPortal(false) }
        )
    }

    if (uiState.isCheckoutDialogOpen && uiState.selectedServiceForHire != null) {
        HireCheckoutDialog(
            service = uiState.selectedServiceForHire!!,
            onDismiss = { viewModel.closeHireCheckout() },
            onConfirm = { method, brief ->
                viewModel.confirmClientHire(method, brief)
            }
        )
    }

    if (uiState.isPaymentConfigOpen) {
        PaymentConfigDialog(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { viewModel.openPaymentConfig(false) }
        )
    }

    if (uiState.isAuthDialogOpen) {
        AuthDialog(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { viewModel.openAuthDialog(false) }
        )
    }

    if (uiState.isTermsDialogOpen) {
        TermsAndRulesDialog(
            isAccepted = uiState.hasAcceptedTerms,
            onAccept = { viewModel.acceptTerms() },
            onDismiss = { viewModel.openTermsDialog(false) }
        )
    }

    if (uiState.isCallActive) {
        CallDialog(
            callerName = uiState.activeCallPartner,
            callerAvatar = uiState.activeCallAvatar,
            isVideoCall = uiState.isCallVideo,
            onEndCall = { viewModel.endCall() }
        )
    }

    if (uiState.isCameraDialogOpen) {
        CameraRecordingDialog(
            onDismiss = { viewModel.openCameraDialog(false) },
            onPublishReel = { caption, uri -> viewModel.publishRecordedReel(caption, uri) },
            onPublishStory = { caption, url -> viewModel.publishRecordedStory(caption, url) }
        )
    }

    if (isCreateHubOpen) {
        CreateHubDialog(
            onDismiss = { isCreateHubOpen = false },
            onOpenRecordCamera = { viewModel.openCameraDialog(true) },
            onOpenAiStudio = {
                viewModel.selectTab(2)
                viewModel.setStudioSubTab(com.example.viewmodel.StudioSubTab.HOOK_STUDIO)
            },
            onCreatePost = { viewModel.selectTab(0) },
            onOpenCreatorDashboard = {
                viewModel.selectTab(2)
                viewModel.setStudioSubTab(com.example.viewmodel.StudioSubTab.CREATOR_DASHBOARD)
            }
        )
    }

    if (isExploreOpen) {
        ExploreModalDialog(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { isExploreOpen = false }
        )
    }

    if (isAppMenuOpen) {
        AppMenuScreen(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { isAppMenuOpen = false },
            onNavigateToProfile = {
                isAppMenuOpen = false
                viewModel.selectTab(4)
            },
            onOpenSearch = {
                isAppMenuOpen = false
                isExploreOpen = true
            }
        )
    }

    // Creator Monetization Super Thanks Dialog
    SuperThanksDialog(
        isOpen = uiState.isSuperThanksDialogOpen,
        videoTitle = uiState.activeTippingVideoTitle,
        creatorName = uiState.activeTippingCreatorName,
        selectedTier = uiState.selectedGiftTier,
        onSelectTier = { viewModel.selectGiftTier(it) },
        onSendTip = { tier, message -> viewModel.sendSuperThanksTip(tier, message) },
        onDismiss = { viewModel.closeSuperThanksDialog() }
    )

    // Creator Earnings Withdrawal Dialog
    WithdrawalDialog(
        isOpen = uiState.isPayoutModalOpen,
        availableBalance = uiState.monetization.availableBalance,
        onRequestWithdrawal = { amount, gateway, dest ->
            viewModel.requestWithdrawal(amount, gateway, dest)
        },
        onDismiss = { viewModel.openPayoutModal(false) }
    )

    // Creator Mindset & Value Hub Dialog (Productivity vs. Consumption)
    if (uiState.isCreatorMindsetDialogOpen) {
        CreatorMindsetDialog(
            isOpen = uiState.isCreatorMindsetDialogOpen,
            minutesConsumed = uiState.creatorMinutesConsumedToday,
            minutesCreated = uiState.creatorMinutesCreatedToday,
            availableBalance = uiState.monetization.availableBalance,
            escrowOrders = uiState.escrowOrders,
            onReleaseMilestone = { orderId, milestoneTitle ->
                viewModel.releaseMilestone(orderId, milestoneTitle)
            },
            onLaunchHookStudio = {
                viewModel.openCreatorMindsetDialog(false)
                viewModel.selectTab(2)
                viewModel.setStudioSubTab(com.example.viewmodel.StudioSubTab.HOOK_STUDIO)
            },
            onOpenMonetization = {
                viewModel.openCreatorMindsetDialog(false)
                viewModel.openPayoutModal(true)
            },
            onDismiss = { viewModel.openCreatorMindsetDialog(false) }
        )
    }

    // Fan Creator Tip Dialog (Tipping from Feed & Posts)
    if (uiState.isTipDialogOpen) {
        CreatorTipDialog(
            isOpen = uiState.isTipDialogOpen,
            creatorName = uiState.tipTargetCreatorName,
            creatorAvatar = uiState.tipTargetCreatorAvatar,
            contentTitle = uiState.tipTargetContentTitle,
            onSendTip = { amount, gateway, message ->
                viewModel.processFanTip(amount, gateway, message)
            },
            onDismiss = { viewModel.closeTipDialog() }
        )
    }

    // Subscription & VIP Membership Tiers Dialog
    if (uiState.isSubscriptionModalOpen) {
        SubscriptionTiersDialog(
            isOpen = uiState.isSubscriptionModalOpen,
            currentTierId = uiState.activeSubscriptionTierId,
            tiers = uiState.subscriptionTiers,
            onSelectTier = { tier, isYearly ->
                viewModel.selectSubscriptionTier(tier, isYearly)
            },
            onDismiss = { viewModel.openSubscriptionModal(false) }
        )
    }

    // Status Notification Banner (e.g. Withdrawal or Tip Sent)
    uiState.payoutStatusNotice?.let { notice ->
        LaunchedEffect(notice) {
            delay(4000)
            viewModel.clearPayoutNotice()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xF00E1724))
                    .border(1.dp, TelegramEmerald, RoundedCornerShape(12.dp))
                    .clickable { viewModel.clearPayoutNotice() }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(notice, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
