package `in`.rahul.videostreamer.model

import com.google.gson.annotations.SerializedName

class VideoModel {
    //(val thumbnail:String, val title:String, val description:String, val videoUrl:String)
    @SerializedName("description")
    val description: String? = null

    @SerializedName("thumb")
    val thumb: String? = null

    @SerializedName("title")
    val title: String? = null

    @SerializedName("url")
    val url: String? = null

    @SerializedName("id")
    val id: String? = null
}