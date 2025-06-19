package com.example.playmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.geometry.Offset
import androidx.navigation.compose.*
import kotlin.system.exitProcess


class MainActivity : ComponentActivity() {
    private val packingListViewModel: PackingListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "main")  {
                composable("main") { MainScreen(navController, packingListViewModel) }
                composable("nextPage") { NextScreen(navController, packingListViewModel) }
            }

        }
    }
}

@Composable
fun MainScreen(navController: NavController, viewModel: PackingListViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var songTitle by remember { mutableStateOf("") }
    var artistName by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }


    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
        .background(Color.Black),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        PulsingShadowText()

            // adds to playlist
            Button(onClick = { showDialog = true },colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Add to playlist", color = Color.Cyan)
            }
            // navigates to next page
            Button(onClick = { navController.navigate("nextPage")},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                Text("View playlist", color = Color.Cyan)
            }
            // exits application
            Button(
                onClick = {
                    // this closes the main activity
                    MainActivity().finish()
                    // this closes the application
                    exitProcess(0)
                },colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Exit", color = Color.Red)
            }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Updated playlist") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = songTitle,
                            onValueChange = { songTitle = it },
                            label = { Text("Song Title") })
                        OutlinedTextField(
                            value = artistName,
                            onValueChange = { artistName = it },
                            label = { Text("Artist Name") })
                        if (rating != 6.toString() ) {
                            OutlinedTextField(
                                value = rating,
                                onValueChange = { rating = it },
                                label = { Text("Rating (1-5)") })
                        }
                        else {
                            OutlinedTextField(
                                value = rating,
                                onValueChange = { rating = it },
                                label = { Text("Rating (1-5)") },
                                supportingText = {
                                    //Text(errorMessage)
                                    Text(
                                        text = "Enter a number between 1 - 5",
                                        style = TextStyle(
                                            color = Color.Red,

                                        )
                                    )
                                }
                            )
                        }

                        OutlinedTextField(
                            value = comments,
                            onValueChange = { comments = it },
                            label = { Text("Comments") })
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (songTitle.isNotBlank() && artistName.isNotBlank() && rating.toIntOrNull() != null && rating <= 5.toString()) {
                            viewModel.items.add(
                                mapOf(
                                    "songTitle" to songTitle,
                                    "artistName" to artistName,
                                    "rating" to rating,
                                    "comments" to comments
                                )
                            )
                            showDialog = false
                            songTitle = ""
                            artistName = ""
                            rating = ""
                            comments = ""
                        }
                    },colors = ButtonDefaults.buttonColors(containerColor = Color.Black)){
                        Text("Confirm", color = Color.Cyan)
                    }
                }
            )
        }
    }
}

@Composable
fun NextScreen(navController: NavController, viewModel: PackingListViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Playlist",
            style = TextStyle(
                color = Color.Black, // Black text color
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = Color.Cyan, // Glow color
                    offset = Offset(0f, 0f), // No offset
                    blurRadius = 20f // Adjust blur radius for glow effect
                )
            ),
            textAlign = TextAlign.Center // Center the text
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (item in viewModel.items) {
                // Safely access the item properties
                val songTitle = item["songTitle"] as? String ?: "Unknown"
                val artistName = item["artistName"] as? String ?: "N/A"
                val rating = item["rating"] as? String ?: "0"
                val comments = item["comments"] as? String ?: "N/A"

                // Display the values using Text composable
                Text("Song Title: $songTitle", color = Color.White)
                Text("Artist Name: $artistName", color = Color.White)
                Text("Rating: $rating", color = Color.White)
                Text("Comments: $comments", color = Color.White)
            }
            Button(onClick = { navController.navigateUp() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Return to main screen", color = Color.Cyan)
            }
        }

    }
}

@Composable
fun PulsingShadowText() {
    // Define the animation for the blur radius
    val transition = rememberInfiniteTransition()
    val blurRadius by transition.animateFloat(
        initialValue = 10f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Text with pulsing shadow effect
    Text(
        text = "Audiocity",
        style = TextStyle(
            color = Color.Black,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            shadow = Shadow(
                color = Color.Cyan,
                offset = Offset(2f, 2f),
                blurRadius = blurRadius // Use animated blur radius
            )
        ),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(30.dp))
}
