package com.giyadabi.appat.ui.Report

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Report(
    val id: String?,
    val uid:String,
    val reportImageUrl:String,
    val rating: String,
    val location: String,
    val date: String,
    val description: String,
    val verification: Boolean
) :Parcelable
{
    constructor():this("","","","","","",
    "",false)
}
