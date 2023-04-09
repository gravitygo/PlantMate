package com.plantmate.plantmate.CallBack

import com.github.mikephil.charting.data.Entry

interface EntryCallback {
    fun onSuccess(data: List<Map<String, Any>>)
    fun onFailure(exception: Exception)
}