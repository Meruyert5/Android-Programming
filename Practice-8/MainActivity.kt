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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.random.Random
import kotlin.math.sin
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.rotate

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

val PastelPurple = Color(0xFFC3B1E1)
val PastelPink = Color(0xFFF9D9F7)
val PastelBlue = Color(0xFFB4D0E7)
val PastelGreen = Color(0xFFC8E6C9)
val PastelOrange = Color(0xFFFFCCBC)
val PastelYellow = Color(0xFFFFF9C4)
val FollowGreen = Color(0xFF4CAF50)
val UnfollowGrey = Color(0xFFE0E0E0)
val LightBlue = Color(0xFFBBDEFB)
val StoryRingColor = Color(0xFFC8E6C9)
val DarkBackground = Color(0xFF121212)
val DarkCard = Color(0xFF1C1C1C)
val DarkText = Color(0xFFEAEAEA)
val SubtleDrawerColor = Color(0xFFF5F5F5)
val SubtleDarkDrawerColor = Color(0xFF242424)

val SoftLavender = Color(0xFFE6E6FA)
val MintCream = Color(0xFFF0FFF0)
val PeachPuff = Color(0xFFFFDAB9)
val PowderBlue = Color(0xFFB0E0E6)
val Thistle = Color(0xFFD8BFD8)
val Honeydew = Color(0xFFF0FFF0)
val AliceBlue = Color(0xFFF0F8FF)
val SeaShell = Color(0xFFFFF5EE)
val LightMint = Color(0xFFB5EAD7)
val SoftPeach = Color(0xFFFFE5D9)

val PurpleGradient = Brush.linearGradient(
    colors = listOf(PastelPurple, PastelPink)
)
val BlueGradient = Brush.linearGradient(
    colors = listOf(PastelBlue, PastelPurple)
)

data class Story(val id: Int, val title: String, val iconResId: Int, val storyContentIds: List<Int>)

val storyList = listOf(
    Story(1, "Work", R.drawable.ic_work, listOf(R.drawable.ic_work, R.drawable.ic_work2, R.drawable.ic_work3)),
    Story(2, "School", R.drawable.ic_school, listOf(R.drawable.ic_school, R.drawable.ic_school2, R.drawable.ic_school3)),
    Story(3, "Travel", R.drawable.ic_travel, listOf(R.drawable.ic_travel, R.drawable.ic_travel2, R.drawable.ic_travel3)),
    Story(4, "Hobbies", R.drawable.ic_hobbies, listOf(R.drawable.ic_hobbies, R.drawable.ic_hobbies2, R.drawable.ic_hobbies3)),
    Story(5, "Pets", R.drawable.ic_pets, listOf(R.drawable.ic_pets, R.drawable.ic_pets2, R.drawable.ic_pets3)),
    Story(6, "Food", R.drawable.ic_food, listOf(R.drawable.ic_food, R.drawable.ic_food2, R.drawable.ic_food3)),
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

val creativeFollowerNames = listOf(
    "Alex K.", "Sarah M.", "David L.", "Emma W.", "James B.",
    "Sophia T.", "Michael R.", "Olivia C.", "Daniel P.", "Isabella M.",
    "William H.", "Mia S.", "Ethan G.", "Charlotte L.", "Benjamin K."
)

val availableAvatars = listOf(
    R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3,
    R.drawable.avatar_4, R.drawable.avatar_5, R.drawable.avatar_6,
    R.drawable.avatar_7, R.drawable.avatar_8
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        when (selectedTab) {
                            0 -> "🌟 Profile"
                            1 -> "📱 Feeds"
                            else -> "App"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PastelPurple,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = PastelPurple.copy(alpha = 0.1f),
                contentColor = PastelPurple,
                modifier = Modifier.height(48.dp)
            ) {
                listOf("Profile", "Feeds").forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }

            when (selectedTab) {
                0 -> ProfileApp()
                1 -> FeedsScreen()
            }
        }
    }
}

@Composable
fun FeedsScreen() {
    var posts by remember { mutableStateOf(getSamplePosts()) }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = PastelPurple)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Feed,
                    contentDescription = "Feed",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Social Feed",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(posts) { post ->
                var currentPost by remember { mutableStateOf(post) }

                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        animationSpec = tween(durationMillis = 800, delayMillis = 200 * post.post.id),
                        initialOffsetY = { it * 5 }
                    ) + fadeIn(tween(800, delayMillis = 200 * post.post.id)),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = post.userAvatar),
                                    contentDescription = "User Avatar",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = post.userName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = post.post.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = post.post.body,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            currentPost = currentPost.copy(likes = currentPost.likes + 1)
                                            posts = posts.map {
                                                if (it.post.id == currentPost.post.id) currentPost else it
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Favorite,
                                            contentDescription = "Like",
                                            tint = if (currentPost.likes > 0) Color.Red else Color.Gray
                                        )
                                    }
                                    Text(
                                        text = "${currentPost.likes} likes",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            val newComment = Comment(
                                                id = currentPost.comments.size + 1,
                                                userName = "You",
                                                text = "Nice post!",
                                                timestamp = "Just now"
                                            )
                                            val updatedComments = currentPost.comments.toMutableList().apply {
                                                add(newComment)
                                            }
                                            currentPost = currentPost.copy(
                                                comments = updatedComments
                                            )
                                            posts = posts.map {
                                                if (it.post.id == currentPost.post.id) currentPost else it
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ModeComment,
                                            contentDescription = "Comments",
                                            tint = Color.Gray
                                        )
                                    }
                                    Text(
                                        text = "${currentPost.comments.size} comments",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            if (currentPost.comments.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Comments:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                currentPost.comments.forEach { comment ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = comment.userName,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = PastelPurple
                                        )
                                        Text(
                                            text = comment.text,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            text = comment.timestamp,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class PostWithUser(
    val post: Post,
    val userName: String,
    val userAvatar: Int,
    var likes: Int = 0,
    val comments: MutableList<Comment> = mutableListOf()
)

private fun getSamplePosts(): List<PostWithUser> {
    return listOf(
        PostWithUser(
            post = Post(1, 1, "Welcome to SDU! 🎓",
                "Excited to start my computer science journey at SDU University! Looking forward to learning Android development and building amazing apps."),
            userName = "Aigerim K.",
            userAvatar = R.drawable.avatar_1,
            likes = 24,
            comments = mutableListOf(
                Comment(1, "Bekzat M.", "Welcome to SDU! 🎉", "2 hours ago"),
                Comment(2, "Chingiz T.", "Which courses are you taking?", "1 hour ago")
            )
        ),
        PostWithUser(
            post = Post(2, 2, "Android Development Progress",
                "Just completed my first Jetpack Compose app! The declarative UI approach is so much better than the old XML way. #AndroidDev #Compose"),
            userName = "Dana S.",
            userAvatar = R.drawable.avatar_4,
            likes = 42,
            comments = mutableListOf(
                Comment(3, "Erlan Z.", "Compose is amazing! 🚀", "5 hours ago"),
                Comment(4, "Farida A.", "Can you share some tips?", "3 hours ago")
            )
        ),
        PostWithUser(
            post = Post(3, 3, "Kazakhstan Tech Scene",
                "The tech industry in Kazakhstan is growing so fast! Proud to be part of this community and excited for the future. #Kazakhstan #Tech"),
            userName = "Gani B.",
            userAvatar = R.drawable.avatar_7,
            likes = 89,
            comments = mutableListOf(
                Comment(5, "Kuat K.", "So true! 🇰🇿", "1 hour ago"),
                Comment(6, "Aigerim K.", "What companies are you interested in?", "45 mins ago")
            )
        ),
        PostWithUser(
            post = Post(4, 4, "Weekend Coding Session",
                "Spent the weekend building this profile app with Jetpack Compose. So satisfying to see everything come together! #Programming #Kotlin"),
            userName = "Bekzat M.",
            userAvatar = R.drawable.avatar_2,
            likes = 156,
            comments = mutableListOf(
                Comment(7, "Chingiz T.", "The app looks great! 👏", "8 hours ago"),
                Comment(8, "Dana S.", "Are you using any specific architecture?", "6 hours ago")
            )
        ),
        PostWithUser(
            post = Post(5, 5, "Almaty Mountains 🏔️",
                "Beautiful day hiking in the mountains near Almaty. Sometimes you need to disconnect from coding and enjoy nature! #Almaty #Kazakhstan"),
            userName = "Farida A.",
            userAvatar = R.drawable.avatar_6,
            likes = 203,
            comments = mutableListOf(
                Comment(9, "Erlan Z.", "The view looks incredible! 😍", "4 hours ago"),
                Comment(10, "Gani B.", "Which trail did you take?", "2 hours ago")
            )
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ProfileApp() {
    val profileViewModel: ProfileViewModel = hiltViewModel()
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
        targetValue = if (isDarkMode) DarkBackground else Honeydew,
        animationSpec = tween(300)
    )
    val cardColor by animateColorAsState(
        targetValue = if (isDarkMode) DarkCard else Color.White,
        animationSpec = tween(300)
    )
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerContainerColor = if (isDarkMode) SubtleDarkDrawerColor else SoftLavender
    val drawerContentColor = if (isDarkMode) DarkText else Color.Black

    val drawerScrollState = rememberScrollState()

    val snackbarMessage by profileViewModel.snackbarMessage.collectAsState()
    val showSnackbar by profileViewModel.showSnackbar.collectAsState()

    LaunchedEffect(showSnackbar) {
        if (showSnackbar && snackbarMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(snackbarMessage)
            profileViewModel.resetSnackbar()
        }
    }

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

                    Spacer(modifier = Modifier.height(16.dp))

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

                    DrawerItem(title = "✨ About Me", onClick = { scope.launch { selectedSection = 0; drawerState.close() } })
                    DrawerItem(title = "💻 Skills", onClick = { scope.launch { selectedSection = 1; drawerState.close() } })
                    DrawerItem(title = "🚀 Projects", onClick = { scope.launch { selectedSection = 2; drawerState.close() } })

                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                    DrawerItem(title = "🔄 Refresh Data", onClick = {
                        scope.launch {
                            profileViewModel.refreshUserData()
                            drawerState.close()
                        }
                    })
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            if (!isDarkMode) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(PowderBlue.copy(alpha = 0.1f), Color.Transparent),
                                        center = Offset(size.width * 0.8f, size.height * 0.2f)
                                    ),
                                    radius = size.width * 0.3f
                                )
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(SoftLavender.copy(alpha = 0.1f), Color.Transparent),
                                        center = Offset(size.width * 0.2f, size.height * 0.7f)
                                    ),
                                    radius = size.width * 0.2f
                                )
                            }
                        }
                )
            }

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("\uD83D\uDCA1 Profile View", fontWeight = FontWeight.Bold)
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = if (isDarkMode) PastelYellow else Color.White
                                )
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
                                    if (showFollowers) Icons.Default.Close else Icons.AutoMirrored.Filled.List,
                                    contentDescription = if (showFollowers) "Hide Followers List" else "Show Followers List",
                                    tint = if (isDarkMode) PastelYellow else Color.White
                                )
                            }

                            IconButton(onClick = { isDarkMode = !isDarkMode }) {
                                Icon(
                                    if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Toggle Theme",
                                    tint = if (isDarkMode) PastelYellow else Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = if (isDarkMode) Color(0xFF2D2D2D) else PastelPurple,
                            titleContentColor = if (isDarkMode) PastelYellow else Color.White,
                            actionIconContentColor = if (isDarkMode) PastelYellow else Color.White,
                            navigationIconContentColor = if (isDarkMode) PastelYellow else Color.White
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
                    onHideFollowers = { showFollowers = false },
                    viewModel = profileViewModel
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
        modifier = Modifier
            .width(80.dp)
            .clickable { onStoryClick(story.id) }
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(PastelPurple, PastelPink)
                    ),
                    shape = CircleShape
                )
                .padding(4.dp)
                .clip(CircleShape)
                .background(if (isDarkMode) DarkCard else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = story.iconResId),
                contentDescription = story.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            story.title,
            style = MaterialTheme.typography.labelSmall,
            color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.DarkGray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StoriesCarousel(isDarkMode: Boolean, onStoryClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) DarkCard else Color.White
        )
    ) {
        Column {
            Text(
                text = "✨ My Stories",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp),
                color = if (isDarkMode) DarkText else Color.Black
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = storyList,
                    key = { it.id }
                ) { story ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInHorizontally(
                            animationSpec = tween(1500, delayMillis = 100 * story.id),
                            initialOffsetX = { it * 5 }
                        ) + fadeIn(tween(300, delayMillis = 100 * story.id)),
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        StoryItem(story, isDarkMode, onStoryClick)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FullScreenStoryViewer(
    stories: List<Story>,
    initialStoryId: Int,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val slideDuration = 500
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
                        progress = { progress },
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
                AnimatedContent(
                    targetState = currentSlideContentId,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
                    }
                ) { targetContentId ->
                    Image(
                        painter = painterResource(id = targetContentId),
                        contentDescription = "Story Content: ${safeCurrentStory.title} Slide ${currentSlideIndex + 1}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
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
        targetValue = if (isFollowing) UnfollowGrey else FollowGreen
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
    onHideFollowers: () -> Unit,
    viewModel: ProfileViewModel
) {
    val followers by viewModel.followers.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val removedFollowerIds = remember { mutableStateOf(setOf<Int>()) }
    val scope = rememberCoroutineScope()

    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followerCount by rememberSaveable { mutableStateOf(999) }
    var showUnfollowDialog by rememberSaveable { mutableStateOf(false) }
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }

    var showConfetti by remember { mutableStateOf(false) }

    var isSyncing by remember { mutableStateOf(false) }
    val avatarScale by animateFloatAsState(
        targetValue = if (isSyncing) 1.1f else 1f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "avatarScale"
    )

    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    var statsVisible by remember { mutableStateOf(false) }
    val statsAlpha by animateFloatAsState(
        targetValue = if (statsVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 800, delayMillis = 300),
        label = "statsAlpha"
    )

    LaunchedEffect(Unit) {
        delay(300)
        statsVisible = true
    }

    LaunchedEffect(userProfile, followers.size) {
        isSyncing = true
        delay(800)
        isSyncing = false
    }

    val buttonColor by animateColorAsState(
        targetValue = if (isFollowing) UnfollowGrey else FollowGreen,
        animationSpec = tween(300),
        label = "buttonColor"
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
                    pulseColor.copy(alpha = 1.0f) at 0
                    pulseColor.copy(alpha = 1.0f) at 2900
                    Color.Transparent at 3000
                }
            )
        }
    }

    val currentHighlightColor = highlightColorAnimatable.value

    val cardElevation = if (isDarkMode) 0.dp else 8.dp
    val textBaseColor = if (isDarkMode) DarkText else Color.Unspecified

    val followerCountColor = if (followerCount >= 1000) Color.Black else Color.Gray

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            state = lazyListState
        ) {
            item(key = "StoriesCarousel") {
                StoriesCarousel(isDarkMode = isDarkMode, onStoryClick = onStoryClick)
            }

            item(key = "ProfileHeader") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(cardElevation),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 3.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(PastelPurple, PastelPink, PastelBlue)
                                        ),
                                        shape = CircleShape
                                    )
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(if (isDarkMode) DarkCard else PastelYellow.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_avatar),
                                    contentDescription = "Meruyert's Profile Image",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .graphicsLayer {
                                            scaleX = avatarScale
                                            scaleY = avatarScale
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = -2.dp, y = 4.dp)
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(FollowGreen.copy(alpha = pulseAlpha))
                                    .border(3.dp, if (isDarkMode) DarkCard else Color.White, CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Meruyert",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = textBaseColor
                            )
                            Spacer(modifier = Modifier.width(2.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    "🔔",
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Switch(
                                    checked = notificationsEnabled,
                                    onCheckedChange = {
                                        notificationsEnabled = it
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                if (it) "🔔 Notifications enabled" else "🔕 Notifications disabled",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    },
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                        }

                        Text(
                            text = "👥 $followerCount Followers",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) PastelYellow else followerCountColor,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .graphicsLayer { alpha = statsAlpha }
                        )

                        Text(
                            text = "Computer Science student at SDU \uD83D\uDC8C",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 12.dp),
                            textAlign = TextAlign.Center,
                            color = textBaseColor,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        var followButtonPressed by remember { mutableStateOf(false) }
                        val followButtonScale by animateFloatAsState(
                            targetValue = if (followButtonPressed) 0.85f else 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow,
                                visibilityThreshold = 0.01f
                            ),
                            label = "followButtonScale"
                        )

                        Button(
                            onClick = {
                                if (isFollowing) {
                                    showUnfollowDialog = true
                                } else {
                                    isFollowing = true
                                    followerCount = followerCount + 1
                                    showConfetti = true
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "🎉 Now following Meruyert!",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = buttonColor,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .height(44.dp)
                                .graphicsLayer {
                                    scaleX = followButtonScale
                                    scaleY = followButtonScale
                                }
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            followButtonPressed = true
                                            tryAwaitRelease()
                                            followButtonPressed = false
                                        }
                                    )
                                },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                if (isFollowing) "Following" else "👤 Follow Meruyert",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (selectedSection != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(
                                        color = currentHighlightColor.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(16.dp)
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
                    enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(400)) +
                            fadeIn(tween(400)),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(400)) +
                            fadeOut(tween(400))
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
                                text = "👥 Followers (${followers.size})",
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
                                        viewModel.removeFollower(removedId)
                                        removedFollowerIds.value = removedFollowerIds.value - removedId
                                        val result = snackbarHostState.showSnackbar(
                                            message = "${follower.name} removed.",
                                            actionLabel = "Undo",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.addFollower(follower)
                                        }
                                    }
                                }

                                AnimatedVisibility(
                                    visible = !isBeingRemoved,
                                    enter = fadeIn(tween(150)) + slideInVertically(tween(300)),
                                    exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
                                ) {
                                    Column {
                                        FollowerItem(
                                            follower = follower,
                                            isDarkMode = isDarkMode,
                                            onRemove = onRemoveFollower
                                        )
                                        HorizontalDivider(
                                            color = Color.Gray.copy(alpha = 0.2f),
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        var buttonPressed by remember { mutableStateOf(false) }
                        val buttonScale by animateFloatAsState(
                            targetValue = if (buttonPressed) 0.95f else 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "buttonScale"
                        )

                        Button(
                            onClick = {
                                val randomName = creativeFollowerNames.random()
                                val randomAvatar = availableAvatars.random()
                                val newFollower = Follower(
                                    id = (followers.maxOfOrNull { it.id } ?: 0) + 1,
                                    name = randomName,
                                    avatarResId = randomAvatar
                                )
                                viewModel.addFollower(newFollower)
                                scope.launch {
                                    snackbarHostState.showSnackbar("🌟 $randomName started following you!")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(50.dp)
                                .graphicsLayer {
                                    scaleX = buttonScale
                                    scaleY = buttonScale
                                }
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            buttonPressed = true
                                            tryAwaitRelease()
                                            buttonPressed = false
                                        }
                                    )
                                },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PastelPurple,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Follower", fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        ConfettiAnimation(
            isActive = showConfetti,
            modifier = Modifier.fillMaxSize(),
            onComplete = { showConfetti = false }
        )
    }

    if (showUnfollowDialog) {
        AlertDialog(
            onDismissRequest = { showUnfollowDialog = false },
            title = { Text("Confirm Unfollow") },
            text = { Text("Are you sure you want to stop following Meruyert?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        isFollowing = false
                        followerCount = followerCount - 1
                        showUnfollowDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Unfollowed Meruyert.", duration = SnackbarDuration.Short)
                        }
                    }
                ) {
                    Text("Unfollow", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnfollowDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SectionContentAbout(isDarkMode: Boolean) {
    Column {
        Text("✨ About Me", fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Passionate about coding, algorithms, and UI design. Love creating beautiful and functional applications that make people's lives easier.", color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(6.dp))
        Text("💡 Always learning new technologies", style = MaterialTheme.typography.bodySmall, color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.Gray)
        Text("🎯 Focused on Android development", style = MaterialTheme.typography.bodySmall, color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.Gray)
        Text("🌟 Enjoy creative problem solving", style = MaterialTheme.typography.bodySmall, color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.Gray)
    }
}

@Composable
fun SectionContentSkills(isDarkMode: Boolean) {
    Column {
        Text("💻 Skills", fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(8.dp))
        Text("SQL · Java · Android · Linux · Data structures", color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(6.dp))
        Text("🎨 UI/UX Design · Compose · Kotlin", style = MaterialTheme.typography.bodySmall, color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.Gray)
        Text("🔧 Git · Retrofit · Room Database", style = MaterialTheme.typography.bodySmall, color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.Gray)
        Text("🌐 REST APIs · JSON · Material Design", style = MaterialTheme.typography.bodySmall, color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.Gray)
    }
}

@Composable
fun SectionContentProjects(isDarkMode: Boolean) {
    Column {
        Text("🚀 Projects", fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Tengrinews demo app · Library Database", color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(6.dp))
        Text("📱 Dynamic Profile App (This one!)", style = MaterialTheme.typography.bodySmall, color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.Gray)
        Text("💾 Room + Retrofit integration", style = MaterialTheme.typography.bodySmall, color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.Gray)
        Text("🎪 Animated Stories feature", style = MaterialTheme.typography.bodySmall, color = if (isDarkMode) DarkText.copy(alpha = 0.8f) else Color.Gray)
    }
}

@Composable
fun ConfettiAnimation(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {}
) {
    var animationTime by remember { mutableStateOf(0L) }
    var confettiPieces by remember { mutableStateOf<List<ConfettiPiece>>(emptyList()) }

    LaunchedEffect(isActive) {
        if (isActive) {
            confettiPieces = List(50) {
                ConfettiPiece(
                    id = it,
                    x = Random.nextFloat() * 1000,
                    startY = 800f + Random.nextFloat() * 200f,
                    speed = 8f + Random.nextFloat() * 6f,
                    rotation = Random.nextFloat() * 360f,
                    rotationSpeed = Random.nextFloat() * 8f - 4f,
                    size = 12f + Random.nextFloat() * 18f,
                    color = when (Random.nextInt(6)) {
                        0 -> PastelPurple
                        1 -> PastelPink
                        2 -> PastelBlue
                        3 -> PastelGreen
                        4 -> PastelOrange
                        else -> PastelYellow
                    }
                )
            }

            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime < 2500) {
                animationTime = System.currentTimeMillis() - startTime
                delay(16)
            }
            onComplete()
        } else {
            confettiPieces = emptyList()
            animationTime = 0L
        }
    }

    if (isActive && confettiPieces.isNotEmpty()) {
        Canvas(modifier = modifier.fillMaxSize()) {
            confettiPieces.forEach { confetti ->
                val progress = animationTime / 2500f
                if (progress > 1f) return@forEach

                val wind = sin(progress * 15f + confetti.id * 0.1f) * 3f
                val newX = confetti.x + wind * 40

                val riseProgress = (progress / 0.4f).coerceAtMost(1f)
                val fallProgress = ((progress - 0.4f) / 0.6f).coerceIn(0f, 1f)

                val calculatedY = if (riseProgress < 1f) {
                    confetti.startY - riseProgress * 400f
                } else {
                    (confetti.startY - 400f) + fallProgress * 600f
                }

                val newRotation = confetti.rotation + confetti.rotationSpeed * progress * 60

                if (calculatedY > -100f && calculatedY < size.height + 100f) {
                    rotate(newRotation) {
                        drawRect(
                            color = confetti.color,
                            topLeft = androidx.compose.ui.geometry.Offset(newX, calculatedY),
                            size = Size(confetti.size, confetti.size / 2)
                        )
                    }
                }
            }
        }
    }
}

data class ConfettiPiece(
    val id: Int,
    val x: Float,
    val startY: Float,
    val speed: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val size: Float,
    val color: Color
)