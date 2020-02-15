package `in`.rahul.videostreamer.presenter

interface VideoAllActivityPresenter {

    fun showProgressLayout()

    fun hideProgressLayout()

    fun showMessage(message: String)

    fun signOut()

    fun getVideoData()
}