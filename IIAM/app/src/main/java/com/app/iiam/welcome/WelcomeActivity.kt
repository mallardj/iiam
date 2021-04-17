package com.app.iiam.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.iiam.R
import com.app.iiam.base.BaseActivity
import com.app.iiam.main.MainActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.signin.SignInActivity
import com.app.iiam.signup.SignupActivity
import com.app.iiam.utils.Const
import com.app.iiam.utils.logInfo
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity() {

    companion object {
        private const val TAG = "WelcomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initView()
        handleClickListner()

    }

    private fun initView() {

    }

    private fun handleClickListner() {

        btnNewUser.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, SignupActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnReturningUser.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, SignInActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }
}