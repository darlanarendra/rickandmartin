package com.clover.rickandmartin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharactersResponse(
    val info: Info,
    val results: List<Result>
): Parcelable {

}