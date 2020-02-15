package `in`.rahul.videostreamer.db

import `in`.rahul.videostreamer.model.VideoModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiHelperInterface {
    @GET("media.json")
    fun videoData(): Call<ArrayList<VideoModel>>

    companion object {
        fun create(): ApiHelperInterface {
            val retrofit = Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiUrlHelper.videoUrl)
                .build()
            return retrofit.create(ApiHelperInterface::class.java)
        }
    }
}