package com.milord.coursework.data

import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("DateTime")
    val date: String,
    @SerializedName("Description")
    val description: String
)
