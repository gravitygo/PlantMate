package com.plantmate.plantmate.objects

class Plant(
    val plantId: Int,
    val plantFamily: String,
    val plantCultivarName: String,
    val plantScientificName: String,
    val plantDescription: String,
    //TODO: Uncomment this when you can show the things in recycler view already
//    val imageType: String,
    val plantStock: Int) {
}