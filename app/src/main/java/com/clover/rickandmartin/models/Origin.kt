package com.clover.rickandmartin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Origin(
    val name: String,
    val url: String
): Parcelable

