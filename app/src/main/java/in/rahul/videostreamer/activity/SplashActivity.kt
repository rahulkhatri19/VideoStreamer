package `in`.rahul.videostreamer.activity

import `in`.rahul.videostreamer.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }, 1500)
    }
}
