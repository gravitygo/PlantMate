package com.plantmate.plantmate.objects

import android.graphics.Bitmap

class Plant(
    val plantId: String,
    val plantFamily: String,
    val plantCultivarName: String,
    val plantScientificName: String,
    val plantDescription: String,
    //TODO: Uncomment this when you can show the things in recycler view already
//    val imageType: Bitmap,
    val plantStock: Int) {
}