package com.example.practice1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val R.drawable.ic_work: Int get() = android.R.drawable.btn_star_big_on
val R.drawable.ic_school: Int get() = android.R.drawable.ic_menu_edit
val R.drawable.ic_travel: Int get() = android.R.drawable.ic_menu_compass
val R.drawable.ic_hobbies: Int get() = android.R.drawable.ic_menu_gallery
val R.drawable.ic_pets: Int get() = android.R.drawable.ic_menu_help
val R.drawable.ic_food: Int get() = android.R.drawable.ic_menu_search
val R.drawable.ic_avatar: Int get() = android.R.drawable.sym_action_chat
val R.drawable.avatar_1: Int get() = android.R.drawable.btn_dialog
val R.drawable.avatar_2: Int get() = android.R.drawable.btn_radio
val R.drawable.avatar_3: Int get() = android.R.drawable.btn_star_big_off
val R.drawable.avatar_4: Int get() = android.R.drawable.btn_default
val R.drawable.avatar_5: Int get() = android.R.drawable.ic_delete
val R.drawable.avatar_6: Int get() = android.R.drawable.ic_dialog_email
val R.drawable.avatar_7: Int get() = android.R.drawable.ic_input_get
val R.drawable.avatar_8: Int get() = android.R.drawable.ic_lock_power_off

data class Story(val id: Int, val title: String, val iconResId: Int, val storyContentIds: List<Int>)

val storyList = listOf(
    Story(1, "Work", R.drawable.ic_work, listOf(R.drawable.ic_work, R.drawable.ic_work, R.drawable.ic_work)),
    Story(2, "School", R.drawable.ic_school, listOf(R.drawable.ic_school, R.drawable.ic_school, R.drawable.ic_school)),
    Story(3, "Travel", R.drawable.ic_travel, listOf(R.drawable.ic_travel, R.drawable.ic_travel, R.drawable.ic_travel)),
    Story(4, "Hobbies", R.drawable.ic_hobbies, listOf(R.drawable.ic_hobbies, R.drawable.ic_hobbies, R.drawable.ic_hobbies)),
    Story(5, "Pets", R.drawable.ic_pets, listOf(R.drawable.ic_pets, R.drawable.ic_pets, R.drawable.ic_pets)),
    Story(6, "Food", R.drawable.ic_food, listOf(R.drawable.ic_food, R.drawable.ic_food, R.drawable.ic_food)),
)

data class Follower(val id: Int, val name: String, val avatarResId: Int)

val initialFollowers = listOf(
    Follower(1, "Aigerim K.", R.drawable.avatar_1),
    Follower(2, "Bekzat M.", R.drawable.avatar_2),
    Follower(3, "Chingiz T.", R.drawable.avatar_3),
    Follower(4, "Dana S.", R.drawable.avatar_4),
    Follower(5, "Erlan Z.", R.drawable.avatar_5),
    Follower(6, "Farida A.", R.drawable.avatar_6),
    Follower(7, "Gani B.", R.drawable.avatar_7),
    Follower(8, "Kuat K.", R.drawable.avatar_8),
)

val PastelPurple = Color(0xFFC3B1E1)
val PastelPink = Color(0xF9D9F7)
val FollowGreen = Color(0xFF4CAF50)
val UnfollowGrey = Color(0xFFE0E0E0)
val LightBlue = Color(0xFFBBDEFB)
val StoryRingColor = Color(0xFFC8E6C9)

val DarkBackground = Color(0xFF121212)
val DarkCard = Color(0xFF1C1C1C)
val DarkText = Color(0xFFEAEAEA)
val SubtleDrawerColor = Color(0xFFF5F5F5)
val SubtleDarkDrawerColor = Color(0xFF242424)



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ProfileApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileApp() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    var isDarkMode by rememberSaveable { mutableStateOf(false) }
    var selectedSection by rememberSaveable { mutableStateOf<Int?>(null) }

    var showStoryViewer by rememberSaveable { mutableStateOf(false) }
    var initialStoryId by rememberSaveable { mutableStateOf(1) }

    var showFollowers by rememberSaveable { mutableStateOf(false) }

    val followerHeaderIndex = 2

    val backgroundColor by animateColorAsState(
        targetValue = if (isDarkMode) DarkBackground else LightBlue,
        label = "BackgroundColor",
        animationSpec = tween(300)
    )
    val cardColor by animateColorAsState(
        targetValue = if (isDarkMode) DarkCard else Color.White,
        label = "CardColor",
        animationSpec = tween(300)
    )
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerContainerColor = if (isDarkMode) SubtleDarkDrawerColor else SubtleDrawerColor
    val drawerContentColor = if (isDarkMode) DarkText else Color.Black

    val drawerScrollState = rememberScrollState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.widthIn(max = 300.dp),
                drawerContainerColor = drawerContainerColor,
                drawerContentColor = drawerContentColor
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(drawerScrollState)
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(PastelPurple, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Profile Star",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "M's Profile Navigator",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Content Sections",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            color = drawerContentColor
                        )
                        IconButton(onClick = {
                            scope.launch { drawerState.close() }
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Close drawer", tint = drawerContentColor)
                        }
                    }

                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))

                    DrawerItem(title = "About Me", onClick = { scope.launch { selectedSection = 0; drawerState.close() } })
                    DrawerItem(title = "Skills", onClick = { scope.launch { selectedSection = 1; drawerState.close() } })
                    DrawerItem(title = "Projects", onClick = { scope.launch { selectedSection = 2; drawerState.close() } })
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Profile View", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                scope.launch {
                                    showFollowers = !showFollowers
                                    if (showFollowers) {
                                        lazyListState.animateScrollToItem(followerHeaderIndex)
                                    }
                                }
                            }) {
                                Icon(
                                    if (showFollowers) Icons.Default.Close else Icons.Default.List,
                                    contentDescription = if (showFollowers) "Hide Followers List" else "Show Followers List"
                                )
                            }

                            IconButton(onClick = { isDarkMode = !isDarkMode }) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = PastelPurple,
                            titleContentColor = if (isDarkMode) DarkText else Color.Black
                        )
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = Color.Transparent
            ) { innerPadding ->
                ProfileScreenContent(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    selectedSection = selectedSection,
                    isDarkMode = isDarkMode,
                    cardColor = cardColor,
                    lazyListState = lazyListState,
                    showFollowers = showFollowers,
                    onStoryClick = { storyId ->
                        initialStoryId = storyId
                        showStoryViewer = true
                    },
                    onHideFollowers = { showFollowers = false }
                )
            }

            if (showStoryViewer) {
                FullScreenStoryViewer(
                    stories = storyList,
                    initialStoryId = initialStoryId,
                    onClose = { showStoryViewer = false }
                )
            }
        }
    }
}

@Composable
fun DrawerItem(title: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(title) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}


@Composable
fun StoryItem(story: Story, isDarkMode: Boolean, onStoryClick: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onStoryClick(story.id) }
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .border(2.dp, StoryRingColor, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
                .background(if (isDarkMode) DarkCard else Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = story.iconResId),
                contentDescription = story.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp).clip(CircleShape)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            story.title,
            style = MaterialTheme.typography.labelSmall,
            color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.DarkGray
        )
    }
}

@Composable
fun StoriesCarousel(isDarkMode: Boolean, onStoryClick: (Int) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = storyList,
            key = { it.id }
        ) { story ->
            StoryItem(story, isDarkMode, onStoryClick)
        }
    }
}

@Composable
fun FullScreenStoryViewer(
    stories: List<Story>,
    initialStoryId: Int,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val slideDuration = 1000
    val initialStoryIndex = stories.indexOfFirst { it.id == initialStoryId }.coerceAtLeast(0)

    var currentStoryIndex by rememberSaveable { mutableStateOf(initialStoryIndex) }
    var currentSlideIndex by rememberSaveable { mutableStateOf(0) }

    val safeCurrentStory = stories.getOrNull(currentStoryIndex) ?: run { onClose(); return }
    val currentStorySlides = safeCurrentStory.storyContentIds.size
    val currentSlideContentId = safeCurrentStory.storyContentIds.getOrNull(currentSlideIndex) ?: run { onClose(); return }

    val progressAnimatable = remember { Animatable(0f) }

    val navigateStory: (Boolean) -> Unit = { isForward ->
        scope.launch {
            progressAnimatable.stop()

            if (isForward) {
                if (currentSlideIndex < currentStorySlides - 1) {
                    currentSlideIndex += 1
                } else if (currentStoryIndex < stories.size - 1) {
                    currentStoryIndex += 1
                    currentSlideIndex = 0
                } else {
                    onClose()
                }
            } else {
                if (currentSlideIndex > 0) {
                    currentSlideIndex -= 1
                } else if (currentStoryIndex > 0) {
                    currentStoryIndex -= 1
                    currentSlideIndex = stories[currentStoryIndex].storyContentIds.size - 1
                }
            }
        }
    }

    val progressKey = "$currentStoryIndex-$currentSlideIndex"

    LaunchedEffect(progressKey) {
        progressAnimatable.stop()
        progressAnimatable.snapTo(0f)

        val animationJob = scope.launch {
            progressAnimatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(slideDuration, easing = LinearEasing)
            )
        }

        animationJob.join()

        if (progressAnimatable.value == 1f) {
            navigateStory(true)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            Modifier.fillMaxSize().systemBarsPadding()
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                safeCurrentStory.storyContentIds.forEachIndexed { index, _ ->
                    val progress = when {
                        index < currentSlideIndex -> 1f
                        index == currentSlideIndex -> progressAnimatable.value
                        else -> 0f
                    }

                    LinearProgressIndicator(
                        progress = progress,
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = safeCurrentStory.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close Story",
                        tint = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { offset ->
                                if (offset.x > size.width / 2) {
                                    navigateStory(true)
                                } else {
                                    navigateStory(false)
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = currentSlideContentId),
                    contentDescription = "Story Content: ${safeCurrentStory.title} Slide ${currentSlideIndex + 1}",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



@Composable
fun FollowerItem(
    follower: Follower,
    isDarkMode: Boolean,
    onRemove: () -> Unit
) {
    var isFollowing by rememberSaveable { mutableStateOf(true) }

    val buttonColor by animateColorAsState(
        targetValue = if (isFollowing) UnfollowGrey else FollowGreen,
        label = "FollowButtonColorAnimation"
    )
    val buttonText = if (isFollowing) "Following" else "Follow"
    val textColor = if (isFollowing) Color.Black else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDarkMode) DarkCard else Color.White)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = follower.avatarResId),
            contentDescription = "${follower.name}'s Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isDarkMode) Color.Gray else PastelPurple)
        )

        Spacer(Modifier.width(12.dp))

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = follower.name,
                fontWeight = FontWeight.Medium,
                color = if (isDarkMode) DarkText else Color.Black
            )

            Button(
                onClick = { isFollowing = !isFollowing },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(buttonText, color = textColor, style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove follower",
                tint = Color.Red.copy(alpha = 0.8f)
            )
        }
    }
}


@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    selectedSection: Int?,
    isDarkMode: Boolean,
    cardColor: Color,
    lazyListState: LazyListState,
    showFollowers: Boolean,
    onStoryClick: (Int) -> Unit,
    onHideFollowers: () -> Unit
) {
    var followers by rememberSaveable { mutableStateOf(initialFollowers) }
    val removedFollowerIds = remember { mutableStateOf(setOf<Int>()) }
    val scope = rememberCoroutineScope()

    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followerCount by rememberSaveable { mutableStateOf(999) }
    var showUnfollowDialog by rememberSaveable { mutableStateOf(false) }
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }

    val buttonColor by animateColorAsState(
        targetValue = if (isFollowing) UnfollowGrey else FollowGreen,
        label = "FollowButtonColorAnimation"
    )
    val buttonText = if (isFollowing) "Unfollow" else "Follow"

    val highlightColorAnimatable = remember { Animatable(Color.Transparent) }

    val pulseColor = PastelPink

    LaunchedEffect(selectedSection) {
        if (selectedSection != null) {
            highlightColorAnimatable.stop()

            highlightColorAnimatable.animateTo(
                targetValue = pulseColor.copy(alpha = 0f),
                animationSpec = keyframes {
                    durationMillis = 3000
                    pulseColor.copy(alpha = 1.0f) at 0 with LinearEasing

                    pulseColor.copy(alpha = 1.0f) at 2900 with LinearEasing

                    Color.Transparent at 3000 with LinearEasing
                }
            )
        }
    }

    val currentHighlightColor = highlightColorAnimatable.value

    val cardElevation = if (isDarkMode) 0.dp else 8.dp
    val textBaseColor = if (isDarkMode) DarkText else Color.Unspecified

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 12.dp),
        state = lazyListState
    ) {
        item(key = "StoriesCarousel") {
            StoriesCarousel(isDarkMode = isDarkMode, onStoryClick = onStoryClick)
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))
        }

        item(key = "ProfileHeader") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(cardElevation),
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_avatar),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Meruyert", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = textBaseColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it; scope.launch { snackbarHostState.showSnackbar(if (it) "Notifications enabled" else "Notifications disabled", duration = SnackbarDuration.Short) } } )
                    }

                    Text(text = "$followerCount Followers", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkText else Color.Black, modifier = Modifier.padding(top = 4.dp))
                    Text("Computer Science student at SDU (づ ᴗ _ᴗ)づ\uD83D\uDC96", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center, color = textBaseColor)
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (isFollowing) { showUnfollowDialog = true } else {
                                isFollowing = true
                                followerCount = followerCount + 1
                                scope.launch { snackbarHostState.showSnackbar("Now following Meruyert!", duration = SnackbarDuration.Short) }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                    ) { Text(buttonText, color = Color.White) }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (selectedSection != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(currentHighlightColor, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            when (selectedSection) {
                                0 -> SectionContentAbout(isDarkMode)
                                1 -> SectionContentSkills(isDarkMode)
                                2 -> SectionContentProjects(isDarkMode)
                            }
                        }
                    }
                }
            }
        }

        item(key = "FollowerSection") {
            AnimatedVisibility(
                visible = showFollowers,
                enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(400)) + fadeIn(tween(400)),
                exit = shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(400)) + fadeOut(tween(400))
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isDarkMode) DarkCard else Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, top = 16.dp, bottom = 8.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Followers (${followers.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = textBaseColor,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        IconButton(onClick = onHideFollowers) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Hide Followers List",
                                tint = textBaseColor
                            )
                        }
                    }

                    Column(Modifier.animateContentSize()) {
                        followers.forEach { follower ->
                            val isBeingRemoved = removedFollowerIds.value.contains(follower.id)

                            val onRemoveFollower: () -> Unit = {
                                scope.launch {
                                    val removedId = follower.id
                                    removedFollowerIds.value = removedFollowerIds.value + removedId

                                    delay(300)

                                    followers = followers.filter { it.id != removedId }
                                    removedFollowerIds.value = removedFollowerIds.value - removedId

                                    val result = snackbarHostState.showSnackbar(
                                        message = "${follower.name} removed.",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        followers = followers.plus(follower).sortedBy { it.id }
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = !isBeingRemoved,
                                enter = fadeIn(tween(150)),
                                exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
                            ) {
                                Column {
                                    FollowerItem(
                                        follower = follower,
                                        isDarkMode = isDarkMode,
                                        onRemove = onRemoveFollower
                                    )
                                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f), modifier = Modifier.padding(horizontal = 16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    if (showUnfollowDialog) {
        AlertDialog(
            onDismissRequest = { showUnfollowDialog = false },
            title = { Text("Confirm Unfollow") },
            text = { Text("Are you sure you want to stop following Meruyert?") },
            confirmButton = {
                TextButton(onClick = {
                    isFollowing = false
                    followerCount = followerCount - 1
                    showUnfollowDialog = false
                    scope.launch { snackbarHostState.showSnackbar("Unfollowed Meruyert.", duration = SnackbarDuration.Short) }
                }) { Text("Unfollow") }
            },
            dismissButton = { TextButton(onClick = { showUnfollowDialog = false }) { Text("Cancel") } }
        )
    }
}



@Composable
fun SectionContentAbout(isDarkMode: Boolean) {
    Column {
        Text("About Me", fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Passionate about coding, algorithms, and UI design.", color = if (isDarkMode) DarkText else Color.Unspecified)
    }
}

@Composable
fun SectionContentSkills(isDarkMode: Boolean) {
    Column {
        Text("Skills", fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(6.dp))
        Text("SQL · Java · Android · Linux · Data structures", color = if (isDarkMode) DarkText else Color.Unspecified)
    }
}

@Composable
fun SectionContentProjects(isDarkMode: Boolean) {
    Column {
        Text("Projects", fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Tengrinews demo app · Library Database", color = if (isDarkMode) DarkText else Color.Unspecified)
    }
}
