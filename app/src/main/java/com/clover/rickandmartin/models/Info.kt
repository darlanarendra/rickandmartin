package com.clover.rickandmartin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Info(
    val count: Int,
    val next: String,
    val pages: Int
    //val prev: Any
): Parcelable {

}
