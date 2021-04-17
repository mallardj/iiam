package com.app.iiam.staticpages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.iiam.R
import com.app.iiam.base.BaseActivity
import kotlinx.android.synthetic.main.activity_static_page.*
import kotlinx.android.synthetic.main.toolbar_home.*

class StaticPageActivity : BaseActivity() {

    private var htmlString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_static_page)

        initView()
        handleClickListner()
    }

    private fun initView() {

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.setText(getString(R.string.terms_and_conditions_title))
        tvToolbarName.visibility = View.VISIBLE

        htmlString = getString(R.string.terms_and_conditions_text)
        webView.loadData(htmlString, "text/html", "UTF-8")

    }

    private fun handleClickListner() {
        ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}