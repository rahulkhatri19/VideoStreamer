package `in`.rahul.videostreamer.activity

import `in`.rahul.videostreamer.R
import `in`.rahul.videostreamer.adapter.VideoAllAdapter
import `in`.rahul.videostreamer.model.VideoModel
import `in`.rahul.videostreamer.utils.ApiHelperInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_video_all.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoAllActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_all)
        recycleView.visibility = View.GONE
        shimmer_layout.visibility = View.VISIBLE
        shimmer_layout.startShimmer()
        getVideoData()
    }

    private fun getVideoData() {
        ApiHelperInterface.create().videoData().enqueue(object : Callback<ArrayList<VideoModel>> {
            override fun onFailure(call: Call<ArrayList<VideoModel>>, t: Throwable) {
                Log.e("VedioAll", ": ${t.stackTrace}")
            }

            override fun onResponse(
                call: Call<ArrayList<VideoModel>>,
                response: Response<ArrayList<VideoModel>>
            ) {
                val videoList: ArrayList<VideoModel> = response.body()!!
                recycleView.layoutManager =
                    LinearLayoutManager(this@VideoAllActivity, LinearLayoutManager.VERTICAL, false)
                recycleView.adapter = VideoAllAdapter(this@VideoAllActivity, videoList)
                shimmer_layout.stopShimmer()
                shimmer_layout.visibility = View.GONE
                recycleView.visibility = View.VISIBLE
            }

        })
    }

    override fun onResume() {
        super.onResume()
        shimmer_layout.startShimmer()
    }

    override fun onPause() {
        shimmer_layout.stopShimmer()
        super.onPause()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_signout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
        if (item.itemId == R.id.signout) {
            signOut()
            return true
        }
        return false
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            Toast.makeText(this, "Signout Successfull", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}