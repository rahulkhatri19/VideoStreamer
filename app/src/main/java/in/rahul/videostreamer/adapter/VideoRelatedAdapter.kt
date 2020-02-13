package `in`.rahul.videostreamer.adapter

import `in`.rahul.videostreamer.R
import `in`.rahul.videostreamer.model.VideoListModel
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.video_related_layout.view.*
import java.util.ArrayList

class VideoRelatedAdapter(val context: Context, val videoList: ArrayList<VideoListModel>):RecyclerView.Adapter<VideoRelatedAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.tv_title
        val tvDescription = view.tv_description
        val ivThumbnail = view.iv_thumbnail
        val clVideo = view.cl_video
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.video_related_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = videoList.get(position)
        holder.tvTitle.text = listItem.title
        holder.tvDescription.text = listItem.description
        Glide.with(context).load(listItem.thumb).into(holder.ivThumbnail)
        val stLog = "title: ${listItem.title}, des: ${listItem.description}, img: ${listItem.thumb}"
        Log.e("ViRelaAd", stLog)
    }
}