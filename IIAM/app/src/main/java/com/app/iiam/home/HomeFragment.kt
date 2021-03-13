package com.app.iiam.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.abilities.AbilitiesActivity
import com.app.iiam.aboutme.AboutMeActivity
import com.app.iiam.base.BaseFragment
import com.app.iiam.conditions.ConditionsActivity
import com.app.iiam.database.viewmodel.UserDetailsViewModel
import com.app.iiam.main.MainActivity
import com.app.iiam.medications.MedicationsActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.records.RecordsActivity
import com.app.iiam.summary.SummaryActivity
import com.app.iiam.utils.Const
import com.app.iiam.utils.logInfo
import com.app.iiam.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private lateinit var userDetailsViewModel: UserDetailsViewModel

    companion object {
        private const val TAG = "HomeFragment"
        fun newInstance(context: Context): HomeFragment {
            val bundle = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleClickListner()
    }

    private fun initView() {
        getUserDetailsFromDB()
    }

    @SuppressLint("SetTextI18n")
    private fun getUserDetailsFromDB() {
        userDetailsViewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)
        userDetailsViewModel.getUserDetails(SharedPrefsManager.getLong(Const.USER_ID)).observe(this, Observer { userDetailsViewModel ->
            userDetailsViewModel.let {
                if (userDetailsViewModel == null) {
                    logInfo(TAG, "No data")
                } else {
                    logInfo(TAG, it.userName)
                    tvName.text = it.userName + ","
                }
            }
        })
    }

    private fun handleClickListner() {

        btnAboutMe.setOnClickListener {
            val intent = Intent(activity, AboutMeActivity::class.java)
            startActivity(intent)
            (activity as MainActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnAbilities.setOnClickListener {
            val intent = Intent(activity, AbilitiesActivity::class.java)
            startActivity(intent)
            (activity as MainActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnMedications.setOnClickListener {
            val intent = Intent(activity, MedicationsActivity::class.java)
            startActivity(intent)
            (activity as MainActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnConditions.setOnClickListener {
            val intent = Intent(activity, ConditionsActivity::class.java)
            startActivity(intent)
            (activity as MainActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnRecords.setOnClickListener {
            val intent = Intent(activity, RecordsActivity::class.java)
            startActivity(intent)
            (activity as MainActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnSummary.setOnClickListener {
            val intent = Intent(activity, SummaryActivity::class.java)
            startActivity(intent)
            (activity as MainActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

}