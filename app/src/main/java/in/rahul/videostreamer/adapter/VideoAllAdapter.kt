package `in`.rahul.videostreamer.adapter

import `in`.rahul.videostreamer.R
import `in`.rahul.videostreamer.activity.VideoPlayerActivity
import `in`.rahul.videostreamer.model.VideoListModel
import `in`.rahul.videostreamer.model.VideoModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.video_main_layout.view.*

class VideoAllAdapter(val context: Context, val videoList: ArrayList<VideoModel>) :
    RecyclerView.Adapter<VideoAllAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.tv_title
        val tvDescription = view.tv_description
        val ivThumbnail = view.iv_thumbnail
        val clVideo = view.cl_video
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.video_main_layout,
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

        holder.clVideo.setOnClickListener {
            val nextVideoList = ArrayList<VideoListModel>()
            for (i in (position+1) until videoList.size){
//val description: String?, val thumb: String?, val title: String?, val url: String?, val id: String?
                nextVideoList.add(VideoListModel(videoList.get(i).description, videoList.get(i).thumb, videoList.get(i).title, videoList.get(i).url, videoList.get(i).id))
            }
            for (i in 0 until position) {
                nextVideoList.add(VideoListModel(videoList.get(i).description, videoList.get(i).thumb, videoList.get(i).title, videoList.get(i).url, videoList.get(i).id))
            }
//            val intent = Intent(context, VideoPlayerActivity::class.java)
            val bundle = Bundle()
            bundle.putString("title", listItem.title)
            bundle.putString("description", listItem.description)
            bundle.putString("vedioUrl", listItem.url)
            bundle.putString("id", listItem.id)
            bundle.putParcelableArrayList("videoList", nextVideoList)
//            intent.putExtras(bundle)
//            intent.putExtra("title", listItem.title)
//            intent.putExtra("vedioUrl", listItem.url)
//            intent.putExtra("id", listItem.id)
            context.startActivity(Intent(context, VideoPlayerActivity::class.java).putExtras(bundle))
        }
    }
}