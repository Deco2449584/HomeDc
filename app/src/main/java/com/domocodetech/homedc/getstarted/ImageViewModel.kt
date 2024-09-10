// Update ImageViewModel to include a loading state
package com.domocodetech.homedc.getstarted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domocodetech.homedc.getstarted.model.ImageData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImageViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _images = MutableStateFlow<List<ImageData>>(emptyList())
    val images: StateFlow<List<ImageData>> = _images

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            _isLoading.value = true
            firestore.collection("images")
                .get()
                .addOnSuccessListener { result ->
                    val imageList = result.map { document ->
                        ImageData(
                            url = document.getString("url") ?: "",
                            name = document.getString("name") ?: "",
                            type = document.getString("type") ?: "",
                            description = document.getString("description") ?: ""
                        )
                    }
                    _images.value = imageList
                    _isLoading.value = false
                }
                .addOnFailureListener { exception ->
                    // Manejar errores
                    _isLoading.value = false
                }
        }
    }
}