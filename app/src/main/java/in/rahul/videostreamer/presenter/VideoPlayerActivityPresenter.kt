package `in`.rahul.videostreamer.presenter

import android.net.Uri
import com.google.android.exoplayer2.source.MediaSource

interface VideoPlayerActivityPresenter {

    fun buildMediaSource(uri: Uri): MediaSource

    fun releasePlayer()

    fun hideSystemUi()

    fun initializePlayer()

    fun loadVideoData()

    fun fetchingReceivedData()
}