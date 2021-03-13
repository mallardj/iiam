package com.app.iiam.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.iiam.R
import com.app.iiam.accountsettings.AccountSettingsFragment
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.UserDetails
import com.app.iiam.database.viewmodel.UserDetailsViewModel
import com.app.iiam.home.HomeFragment
import com.app.iiam.listner.ItemClickListener
import com.app.iiam.models.DrawerDataModel
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.sendsummary.SendSummaryFragment
import com.app.iiam.utils.Const
import com.app.iiam.utils.logInfo
import com.app.iiam.welcome.WelcomeActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_alert_dialog.view.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.toolbar_home.*
import java.util.*

class MainActivity : BaseActivity(), ItemClickListener<DrawerDataModel> {

    private lateinit var userDetailsViewModel: UserDetailsViewModel
    private lateinit var mUserDetails: UserDetails
    lateinit var mAdapter: DrawerItemAdapter

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onStart() {
        super.onStart()

        getUserDetailsFromDB()
    }

    private fun getUserDetailsFromDB() {
        showLoader()

        userDetailsViewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)

        userDetailsViewModel.getUserDetails(SharedPrefsManager.getLong(Const.USER_ID)).observe(this, Observer { userDetailsViewModel ->
            userDetailsViewModel.let {
                if (userDetailsViewModel == null) {
                    logInfo(TAG, "No data")
                } else {
                    logInfo(TAG, it.userName)
                    mUserDetails = userDetailsViewModel

                    Glide.with(this@MainActivity)
                        .asBitmap()
                        .load(it.userProfilePicture)
                        .placeholder(R.drawable.ic_dummy_image)
                        .circleCrop()
                        .into(tvUserPhoto)
                    tvUserName.text = mUserDetails.userName

                }
            }
        })

        hideLoader()
    }

    private val mHomeFragment by lazy {
        HomeFragment.newInstance(this)
    }

    private val mSendSummaryFragment by lazy {
        SendSummaryFragment.newInstance(this, false)
    }

    private val mSendMedicationsSummaryFragment by lazy {
        SendSummaryFragment.newInstance(this, true)
    }

    private val mAccountSettingsFragment by lazy {
        AccountSettingsFragment.newInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        handleClickListner()
        loadHomeFragment()
    }

    private fun initView() {
        setupDrawer()
    }

    private fun handleClickListner() {

        ivToolbarRightIcon.setOnClickListener {
            showSignoutDialog()
        }

    }

    private fun showSignoutDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.item_alert_dialog, null)
        val mBuilder = AlertDialog.Builder(this, R.style.CustomBottomSheetDialogTheme).setView(mDialogView).setCancelable(false)
        val mAlertDialog = mBuilder.show()
        mDialogView.tvDialogTitle.text = getString(R.string.are_you_sure_you_want_to_signout)
        mDialogView.tvDialogDelete.text = getString(R.string.signout)
        mDialogView.tvDialogCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
        mDialogView.tvDialogDelete.setOnClickListener {
            signOut()
            mAlertDialog.dismiss()
        }
    }

    private fun signOut() {

        val remember_me = SharedPrefsManager.getBoolean(Const.REMEMBER_ME)
        val user_email = SharedPrefsManager.getString(Const.USER_EMAIL)
        val user_password = SharedPrefsManager.getString(Const.USER_PASSWORD)

        SharedPrefsManager.clearPrefs()

        SharedPrefsManager.setBoolean(Const.REMEMBER_ME, remember_me)
        SharedPrefsManager.setString(Const.USER_EMAIL, user_email)
        SharedPrefsManager.setString(Const.USER_PASSWORD, user_password)

        val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun setupDrawer() {

        val toggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor =
                        ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor =
                        ContextCompat.getColor(this@MainActivity, R.color.colorGreenBlue)
                }
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor =
                        ContextCompat.getColor(this@MainActivity, R.color.colorGreenBlue)
                }
            }

        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        drawer_layout.setViewScale(Gravity.START, 0.9f)
        drawer_layout.setViewElevation(Gravity.START, 0f)
        drawer_layout.setRadius(Gravity.START, 0f)

        val mData = ArrayList<DrawerDataModel>()
        mData.add(DrawerDataModel(R.drawable.selector_home, getString(R.string.home)))
        mData.add(DrawerDataModel(R.drawable.selector_send_summary, getString(R.string.send_summary)))
        mData.add(DrawerDataModel(R.drawable.selector_medications_list, getString(R.string.send_medications_list)))
        mData.add(DrawerDataModel(R.drawable.selector_account_settings, getString(R.string.account_settings)))

        mAdapter = DrawerItemAdapter(mData, this)

        rvHeaderItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvHeaderItem.adapter = mAdapter
    }

    override fun onItemClicked(position: Int, model: DrawerDataModel?) {
        drawer_layout.closeDrawer(GravityCompat.START)

        when (position) {
            0 -> {
                loadHomeFragment()
            }
            1 -> {
                loadSendSummaryFragment()
            }
            2 -> {
                loadSendMedicationsFragment()
            }
            3 -> {
                loadAccountSettingsFragment()
            }
        }
    }

    private fun loadHomeFragment() {
        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.VISIBLE
        ivBack.visibility = View.GONE
        tvToolbarName.visibility = View.GONE
        loadFragment(mHomeFragment)
    }

    private fun loadSendSummaryFragment() {
        tvToolbarTitle.setText(getString(R.string.send_summary))
        tvToolbarTitle.visibility = View.VISIBLE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.GONE
        tvToolbarName.visibility = View.GONE
        loadFragment(mSendSummaryFragment)
    }

    private fun loadSendMedicationsFragment() {
        tvToolbarTitle.setText(getString(R.string.send_medications_list))
        tvToolbarTitle.visibility = View.VISIBLE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.GONE
        tvToolbarName.visibility = View.GONE
        loadFragment(mSendMedicationsSummaryFragment)
    }

    private fun loadAccountSettingsFragment() {
        tvToolbarTitle.setText(getString(R.string.account_settings))
        tvToolbarTitle.visibility = View.VISIBLE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.GONE
        tvToolbarName.visibility = View.GONE
        loadFragment(mAccountSettingsFragment)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        return fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment)
                .commit()
            true
        } ?: let {
            false
        }
    }

}