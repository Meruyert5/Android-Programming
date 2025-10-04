package com.example.practice1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val PastelPurple = Color(0xFFC3B1E1)
val FollowGreen = Color(0xFF4CAF50)
val UnfollowGrey = Color(0xFFE0E0E0)
val LightBlue = Color(0xFFBBDEFB)

val DarkBackground = Color(0xFF121212)
val DarkCard = Color(0xFF1C1C1C)
val DarkText = Color(0xFFEAEAEA)

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

    var isDarkMode by rememberSaveable { mutableStateOf(false) }
    var selectedSection by rememberSaveable { mutableStateOf<Int?>(null) }

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .widthIn(max = 280.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sections",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) DarkText else Color.Black
                        )
                        IconButton(onClick = {
                            scope.launch { drawerState.close() }
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Close drawer", tint = if (isDarkMode) DarkText else Color.Black)
                        }
                    }

                    HorizontalDivider(color = Color.Gray)

                    DrawerItem(title = "About Me", onClick = {
                        scope.launch {
                            selectedSection = 0
                            drawerState.close()
                        }
                    })

                    DrawerItem(title = "Skills", onClick = {
                        scope.launch {
                            selectedSection = 1
                            drawerState.close()
                        }
                    })

                    DrawerItem(title = "Projects", onClick = {
                        scope.launch {
                            selectedSection = 2
                            drawerState.close()
                        }
                    })
                }
            }
        }
    ) {
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
            containerColor = backgroundColor
        ) { innerPadding ->
            ProfileCard(
                modifier = Modifier.padding(innerPadding),
                snackbarHostState = snackbarHostState,
                selectedSection = selectedSection,
                isDarkMode = isDarkMode,
                cardColor = cardColor
            )
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
fun ProfileCard(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    selectedSection: Int?,
    isDarkMode: Boolean,
    cardColor: Color
) {
    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followerCount by rememberSaveable { mutableStateOf(999) }
    var showUnfollowDialog by rememberSaveable { mutableStateOf(false) }
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    val buttonColor by animateColorAsState(
        targetValue = if (isFollowing) UnfollowGrey else FollowGreen,
        label = "FollowButtonColorAnimation"
    )

    val buttonText = if (isFollowing) "Unfollow" else "Follow"
    val buttonTextColor = if (isFollowing) Color.Black else Color.White

    var highlightPulse by remember { mutableStateOf(false) }
    LaunchedEffect(selectedSection) {
        if (selectedSection != null) {
            highlightPulse = true
            delay(800)
            highlightPulse = false
        }
    }

    val highlightColor = if (isDarkMode) Color(0xFF2A2B2C) else Color(0xFFDADBFF)
    val highlightAnimColor by animateColorAsState(
        targetValue = if (highlightPulse) highlightColor else Color.Transparent,
        animationSpec = tween(300)
    )

    val cardElevation = if (isDarkMode) 0.dp else 8.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 12.dp),
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
                    Text(
                        "Meruyert",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) DarkText else Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = {
                            notificationsEnabled = it
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (it) "Notifications enabled" else "Notifications disabled",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }

                val followerTextColor = when {
                    isDarkMode -> DarkText
                    followerCount >= 1000 -> Color.Black
                    else -> Color.Gray
                }

                Text(
                    text = "$followerCount Followers",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = followerTextColor,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    "Computer Science student at SDU (づ ᴗ _ᴗ)づ\uD83D\uDC96",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center,
                    color = if (isDarkMode) DarkText else Color.Unspecified
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (isFollowing) {
                            showUnfollowDialog = true
                        } else {
                            isFollowing = true
                            followerCount = followerCount + 1
                            scope.launch {
                                snackbarHostState.showSnackbar("Now following Meruyert!", duration = SnackbarDuration.Short)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(buttonText, color = buttonTextColor)
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (selectedSection != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(highlightAnimColor, RoundedCornerShape(12.dp))
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
                    scope.launch {
                        snackbarHostState.showSnackbar("Unfollowed Meruyert.", duration = SnackbarDuration.Short)
                    }
                }) { Text("Unfollow") }
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
        Text("About Me", fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "Passionate about coding, algorithms, and UI design.",
            color = if (isDarkMode) DarkText else Color.Unspecified
        )
    }
}

@Composable
fun SectionContentSkills(isDarkMode: Boolean) {
    Column {
        Text("Skills", fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "SQL · Java · Android · Linux · Data structures",
            color = if (isDarkMode) DarkText else Color.Unspecified
        )
    }
}

@Composable
fun SectionContentProjects(isDarkMode: Boolean) {
    Column {
        Text("Projects", fontWeight = FontWeight.Bold, color = if (isDarkMode) DarkText else Color.Unspecified)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "Tengrinews demo app · Library Database",
            color = if (isDarkMode) DarkText else Color.Unspecified
        )
    }
}
