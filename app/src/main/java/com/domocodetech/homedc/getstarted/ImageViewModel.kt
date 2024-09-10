package com.domocodetech.homedc.getstarted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ImageData(val url: String, val name: String)

class ImageViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _images = MutableStateFlow<List<ImageData>>(emptyList())
    val images: StateFlow<List<ImageData>> = _images

    init {
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            firestore.collection("images")
                .get()
                .addOnSuccessListener { result ->
                    val imageList = result.map { document ->
                        ImageData(
                            url = document.getString("url") ?: "",
                            name = document.getString("name") ?: ""
                        )
                    }
                    _images.value = imageList
                }
                .addOnFailureListener { exception ->
                    // Manejar errores
                }
        }
    }
}