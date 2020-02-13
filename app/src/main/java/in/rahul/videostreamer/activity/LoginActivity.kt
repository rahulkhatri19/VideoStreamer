package `in`.rahul.videostreamer.activity

import `in`.rahul.videostreamer.R
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
        // Choose authentication providers
        val googleProvider = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())


        btn_login.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                    googleProvider
                ).build(), RC_SIGN_IN
            )
//            startActivity(Intent(this, VideoAllActivity::class.java))
        }

//        btn_login.setOnClickListener {
//            AuthUI.getInstance().signOut(this).addOnCompleteListener {
//                Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show()
////                startActivity(Intent(this, MainActivity::class.java))
//            }
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
                val response = IdpResponse.fromResultIntent(data)
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(Intent(this, VideoAllActivity::class.java))
                    val user = auth.currentUser
                    val stLog =
                        "userName: ${user?.displayName}, email: ${user?.email}, uid: ${user?.uid}, pid: ${user?.providerId}, res: $response"
                    Log.e("Login Act", stLog)
                } else {
                    Toast.makeText(this, "Error: $response", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        currentUser.let {
            val stLog =
                "userName: ${currentUser?.displayName}, email: ${currentUser?.email}, uid: ${currentUser?.uid}, pid: ${currentUser?.providerId}"
            Log.e("Login Act Res", stLog)
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
            Toast.makeText(this, "Signout Successfull", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
