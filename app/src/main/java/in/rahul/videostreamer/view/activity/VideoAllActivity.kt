package `in`.rahul.videostreamer.view.activity

import `in`.rahul.videostreamer.R
import `in`.rahul.videostreamer.view.adapter.VideoAllAdapter
import `in`.rahul.videostreamer.model.VideoModel
import `in`.rahul.videostreamer.presenter.VideoAllActivityPresenter
import `in`.rahul.videostreamer.db.ApiHelperInterface
import `in`.rahul.videostreamer.util.CommonUtil
import `in`.rahul.videostreamer.util.CommonUtil.isOnline
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_video_all.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoAllActivity : AppCompatActivity(), VideoAllActivityPresenter {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_all)

        if (!isOnline(this)) {
            internetDialog()
        }

        showProgressLayout()
        getVideoData()

    }

    override fun getVideoData() {
        ApiHelperInterface.create().videoData().enqueue(object : Callback<ArrayList<VideoModel>> {
            override fun onFailure(call: Call<ArrayList<VideoModel>>, t: Throwable) {
                showMessage("Please Try after some time")
            }

            override fun onResponse(
                call: Call<ArrayList<VideoModel>>,
                response: Response<ArrayList<VideoModel>>
            ) {
                val videoList: ArrayList<VideoModel> = response.body()!!
                recycleView.layoutManager =
                    LinearLayoutManager(this@VideoAllActivity, LinearLayoutManager.VERTICAL, false)
                recycleView.adapter = VideoAllAdapter(this@VideoAllActivity, videoList)
                hideProgressLayout()
            }

        })
    }

    override fun onResume() {
        super.onResume()
//        showProgressLayout()
        shimmer_layout.startShimmer()
    }

    override fun onPause() {
        shimmer_layout.stopShimmer()
//        hideProgressLayout()
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

    override fun signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            showMessage("SignOut Successful")
            finish()
        }
    }

    override fun showProgressLayout() {
        recycleView.visibility = View.GONE
        shimmer_layout.visibility = View.VISIBLE
        shimmer_layout.startShimmer()
    }

    override fun hideProgressLayout() {
        shimmer_layout.stopShimmer()
        shimmer_layout.visibility = View.GONE
        recycleView.visibility = View.VISIBLE
    }

    override fun showMessage(message: String) {
        CommonUtil.showMessage(this, message)
    }
    override fun internetDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please Connect to Internet")
        builder.setCancelable(false).setIcon(R.drawable.ic_internet).setTitle("No Internet Connection")
        builder.setPositiveButton("OK"
        ) { dialog, id ->
            dialog.cancel()
            finish()
        }.setNegativeButton("Retry") { dialog, which ->
            if (!isOnline(this)) {
                internetDialog()
            } else {
                dialog.dismiss()
                getVideoData()
            }
        }
        val alert = builder.create()
        alert.show()
    }
}
