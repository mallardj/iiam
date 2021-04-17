package com.app.iiam.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import com.app.iiam.R
import com.app.iiam.base.BaseActivity
import com.app.iiam.main.MainActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import com.app.iiam.welcome.WelcomeActivity

class SplashActivity : BaseActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    companion object {
        const val SPLASH_TIME_OUT: Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
    }

    private fun initView() {

        handler = Handler()
        runnable = Runnable {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            if (SharedPrefsManager.getLong(Const.USER_ID).toString().equals("0")) {
                val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        if (::handler.isInitialized) {
            if (::runnable.isInitialized) {
                handler.postDelayed(
                    runnable,
                    SPLASH_TIME_OUT
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (::handler.isInitialized) {
            if (::runnable.isInitialized) {
                handler.removeCallbacks(runnable)
            }
        }
    }
}