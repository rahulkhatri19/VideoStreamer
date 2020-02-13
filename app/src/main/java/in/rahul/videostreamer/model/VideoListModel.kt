package `in`.rahul.videostreamer.model

import android.os.Parcel
import android.os.Parcelable

class VideoListModel(val description: String?, val thumb: String?, val title: String?, val url: String?, val id: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(thumb)
        parcel.writeString(title)
        parcel.writeString(url)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoListModel> {
        override fun createFromParcel(parcel: Parcel): VideoListModel {
            return VideoListModel(parcel)
        }

        override fun newArray(size: Int): Array<VideoListModel?> {
            return arrayOfNulls(size)
        }
    }
}