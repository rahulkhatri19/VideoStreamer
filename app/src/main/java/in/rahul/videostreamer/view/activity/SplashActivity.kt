package `in`.rahul.videostreamer.view.activity

import `in`.rahul.videostreamer.R
import `in`.rahul.videostreamer.presenter.SplashActivityPresenter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity(), SplashActivityPresenter {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startDestinationActivity()
            finish()
        }, 1500)
    }

    override fun startDestinationActivity() {
        startActivity(
            Intent(
                this,
                LoginActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }
}
