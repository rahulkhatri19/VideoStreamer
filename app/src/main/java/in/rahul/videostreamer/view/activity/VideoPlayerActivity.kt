package `in`.rahul.videostreamer.view.activity

import `in`.rahul.videostreamer.R
import `in`.rahul.videostreamer.view.adapter.VideoRelatedAdapter
import `in`.rahul.videostreamer.model.VideoListModel
import `in`.rahul.videostreamer.presenter.VideoPlayerActivityPresenter
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_video_player.*
import java.util.ArrayList

class VideoPlayerActivity : AppCompatActivity(), VideoPlayerActivityPresenter {

    lateinit var playerView: PlayerView
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private var videoUrl: String? = ""
    private var videoTitle: String? = ""
    private var videoDes: String? = ""
    private var videoId: String? = ""
    private var userUid: String? = ""
    lateinit var nextVideoList: ArrayList<VideoListModel>
//    private var playbackStateListener:PlaybackStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        playerView = findViewById(R.id.exo_player)
        userUid = FirebaseAuth.getInstance().currentUser?.uid

        fetchingReceivedData()
        loadVideoData()

        tv_title.text = videoTitle
        tv_description.text = videoDes
        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = VideoRelatedAdapter(this, nextVideoList)

    }

    override fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this)
        playerView.player = player

        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri)

        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare(mediaSource, false, false)
    }

    override fun loadVideoData() {
        val videoRef = FirebaseDatabase.getInstance().getReference("videoData/$userUid/$videoId")

        videoRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(data: DataSnapshot) {
                val stData =
                    "playWhen: ${data.child("playWhenReady").getValue()}, pos: ${data.child("playbackPosition").getValue()}, win: ${data.child(
                        "currentWindow"
                    ).getValue()}"
                Log.e("vid play data", stData)
                val videoPlayReady = "${data.child("playWhenReady").getValue()}"
                val videoPosition = "${data.child("playbackPosition").getValue()}"
                val videoWindow = "${data.child("currentWindow").getValue()}"
                if (!videoPlayReady.equals("null")) {
                    playWhenReady = videoPlayReady.toBoolean()
                }
                if (!videoPosition.equals("null")) {
                    playbackPosition = videoPosition.toLong()
                }
                if (!videoWindow.equals("null")) {
                    currentWindow = videoWindow.toInt()
                }
                initializePlayer()
            }
        })
    }

    override fun fetchingReceivedData() {
        videoTitle = intent.getStringExtra("title")
        videoDes = intent.getStringExtra("description")
        videoUrl = intent.getStringExtra("vedioUrl")
        videoId = intent.getStringExtra("id")
        nextVideoList = intent.getParcelableArrayListExtra<VideoListModel>("videoList")!!
    }

    override fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, "exoplayer-codelab")
//        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
        val mediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)
        // Create a media source using the supplied URI
        val mediaSource1 = mediaSourceFactory.createMediaSource(uri)
        val concatenatingMediaSource = ConcatenatingMediaSource()

        concatenatingMediaSource.addMediaSource(mediaSource1)

        if (nextVideoList != null) {
            for (i in nextVideoList.indices) {
                Log.e("vp Act", ": ${nextVideoList.get(i).url}")
                val uriList = Uri.parse(nextVideoList.get(i).url)
                val mediaSource = mediaSourceFactory.createMediaSource(uriList)
                concatenatingMediaSource.addMediaSource(mediaSource)
            }
        }
        return concatenatingMediaSource
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            releasePlayer()
        }
    }

    override fun releasePlayer() {

        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex

            val stLog =
                "playready:${player!!.playWhenReady}, currentwind:${player!!.currentWindowIndex}, playPos:${player!!.currentPosition}"

            Log.e("ExoAct2", stLog)

//            DatabaseReference checkOnline = FirebaseDatabase.getInstance().getReference("status");
            val videoDataReference = FirebaseDatabase.getInstance().getReference("videoData")

            val map = HashMap<String, String>()
            map.put("playWhenReady", playWhenReady.toString())
            map.put("playbackPosition", playbackPosition.toString())
            map.put("currentWindow", currentWindow.toString())

            videoDataReference.child(userUid!!).child(videoId!!).setValue(map)
//
//            player!!.removeListener(playbackStateListener)
            player!!.release()
            player = null
        }
    }

    @SuppressLint("InlinedApi")
    override fun hideSystemUi() {
        playerView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
