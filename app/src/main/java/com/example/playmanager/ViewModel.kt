package com.example.playmanager

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

class PackingListViewModel : ViewModel() {
    val items = mutableStateListOf<Map<String, Any>>()
}