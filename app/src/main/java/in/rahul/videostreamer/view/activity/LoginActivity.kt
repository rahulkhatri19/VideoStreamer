package `in`.rahul.videostreamer.view.activity

import `in`.rahul.videostreamer.R
import `in`.rahul.videostreamer.presenter.LoginActivityPresenter
import `in`.rahul.videostreamer.util.CommonUtil
import `in`.rahul.videostreamer.util.CommonUtil.internetDialog
import `in`.rahul.videostreamer.util.CommonUtil.isOnline
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginActivityPresenter {

    val RC_SIGN_IN = 100
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        if (!isOnline(this)) {
            internetDialog(this)
        }

        btn_login.setOnClickListener {
            startFirebaseAuth()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
//                val response = IdpResponse.fromResultIntent(data)
                if (resultCode == Activity.RESULT_OK) {
                    startDestinationActivity()
                    showMessage("SignIn Successful")
                } else {
                    showMessage("Unable to Login\nPlease try After Sometime")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startDestinationActivity()
        }
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

    override fun startDestinationActivity() {
        startActivity(Intent(this, VideoAllActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun startFirebaseAuth() {
        // Choose authentication providers
        val googleProvider = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                googleProvider
            ).build(), RC_SIGN_IN
        )
    }

    override fun showMessage(message: String) {
        CommonUtil.showMessage(this, message)
    }

}
