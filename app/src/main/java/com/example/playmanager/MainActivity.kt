package com.example.playmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
        .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // adds to playlist
            Button(onClick = { showDialog = true }) {
                Text("Add to playlist")
            }
            // navigates to next page
            Button(onClick = { navController.navigate("nextPage")}) {
                Text("View playlist")
            }
            // exits application
            Button(
                onClick = {
                    // this closes the main activity
                    MainActivity().finish()
                    // this closes the application
                    exitProcess(0)
                }
            ) {
                Text("Exit")
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
                    }){
                        Text("Confirm")
                    }
                }
            )
        }
    }
}

@Composable
fun NextScreen(navController: NavController, viewModel: PackingListViewModel) {
    Column(modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { navController.navigateUp() }) {
            Text("Back")
        }
        Column {
            for (item in viewModel.items) {
                // Safely access the item properties
                val songTitle = item["songTitle"] as? String ?: "Unknown"
                val artistName = item["artistName"] as? String ?: "N/A"
                val rating = item["rating"] as? String ?: "0"
                val comments = item["comments"] as? String ?: "N/A"

                // Display the values using Text composable
                Text("Song Title: $songTitle\nArtist Name: $artistName\nRating out of 5: $rating\nComments: $comments")
            }
        }
    }
}
