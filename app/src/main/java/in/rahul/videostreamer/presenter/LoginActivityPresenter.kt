package `in`.rahul.videostreamer.presenter

interface LoginActivityPresenter {

    fun startFirebaseAuth()

    fun showMessage(message: String)

    fun signOut()

    fun startDestinationActivity()

}