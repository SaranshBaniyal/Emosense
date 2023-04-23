package com.example.emosense

import android.os.Parcel
import android.os.Parcelable
//we made this class inherit Parcelable to pass this class's object in intent, with on click action of Recycler View
data class Journal(var id:Int, var username:String, var entry:String, var date:String, var label:String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(username)
        parcel.writeString(entry)
        parcel.writeString(date)
        parcel.writeString(label)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Journal> {
        override fun createFromParcel(parcel: Parcel): Journal {
            return Journal(parcel)
        }

        override fun newArray(size: Int): Array<Journal?> {
            return arrayOfNulls(size)
        }
    }
}
