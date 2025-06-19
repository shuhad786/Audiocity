package com.example.playmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*

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

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { showDialog = true }) {
                Text("Add to playlist")
            }
            Button(onClick = { navController.navigate("nextPage")}) {
                Text("View playlist")
            }
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
                        OutlinedTextField(
                            value = rating,
                            onValueChange = { rating = it },
                            label = { Text("Rating (1-5)") })
                        OutlinedTextField(
                            value = comments,
                            onValueChange = { comments = it },
                            label = { Text("Comments") })
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (songTitle.isNotBlank() && artistName.isNotBlank() && rating.toIntOrNull() != null && rating <= 5.toString()) {
                            viewModel.items.add(mapOf(
                                "songTitle" to songTitle,
                                "artistName" to artistName,
                                "rating" to rating,
                                "comments" to comments
                            ))
                            showDialog = false
                            songTitle = ""
                            artistName = ""
                            rating = ""
                            comments = ""
                        }
                    }){
                        Text("Add")
                    }
                }
            )
        }
    }
}

@Composable
fun NextScreen(navController: NavController, viewModel: PackingListViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
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

