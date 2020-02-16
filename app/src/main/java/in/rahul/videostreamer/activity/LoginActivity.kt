package `in`.rahul.videostreamer.activity

import `in`.rahul.videostreamer.R
import `in`.rahul.videostreamer.utils.CommonUtil
import `in`.rahul.videostreamer.utils.CommonUtil.internetDialog
import `in`.rahul.videostreamer.utils.CommonUtil.isOnline
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val RC_SIGN_IN = 100
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        if (!isOnline(this)) {
            internetDialog(this)
        }
        // Choose authentication providers
        val googleProvider = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())


        btn_login.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                    googleProvider
                ).build(), RC_SIGN_IN
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
                val response = IdpResponse.fromResultIntent(data)
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(Intent(this, VideoAllActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    CommonUtil.showMessage(this,"SignIn Successful")
                    finish()
//                    val user = auth.currentUser
                } else {
                    CommonUtil.showMessage(this, "Unable to Login\nPlease try After Sometime")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this, VideoAllActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
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

    private fun signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            CommonUtil.showMessage(this, "SignOut Successful")
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
