package com.app.iiam.conditions

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.iiam.R
import com.app.iiam.addconditions.AddConditionsActivity
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.Conditions
import com.app.iiam.database.viewmodel.ConditionsViewModel
import com.app.iiam.fullscreenimage.FullScreenImageVideoActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import kotlinx.android.synthetic.main.activity_conditions.*
import kotlinx.android.synthetic.main.item_alert_dialog.view.*
import kotlinx.android.synthetic.main.toolbar_home.*

class ConditionsActivity : BaseActivity(), ConditionsBottomSheetFragment.ItemClickListener, ConditionsAdapter.OnItemClickListener, ConditionBottomSheetFragment.ItemClickListener {

    private lateinit var conditionsViewModel: ConditionsViewModel
    private var mAdapter: ConditionsAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mConditionsList = mutableListOf<Conditions>()
    private lateinit var mConditions: Conditions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conditions)

        initView()
        handleClickListner()
    }

    override fun onStart() {
        super.onStart()

        getConditionsDetailsFromDB()
    }

    private fun initView() {

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.setText(getString(R.string.conditions))
        tvToolbarName.visibility = View.VISIBLE

        linearLayoutManager = LinearLayoutManager(this@ConditionsActivity)
        mAdapter = ConditionsAdapter(mConditionsList, this)
        rvConditions.layoutManager = linearLayoutManager
        rvConditions.adapter = mAdapter
    }

    private fun getConditionsDetailsFromDB() {
        showLoader()

        val mUserId = SharedPrefsManager.getLong(Const.USER_ID)

        conditionsViewModel = ViewModelProvider(this).get(ConditionsViewModel::class.java)

        conditionsViewModel.getAllConditions(mUserId).observe(this, Observer { conditions ->

            mConditionsList.clear()
            mConditionsList.addAll(conditions)

            if (mConditionsList.size != 0) {
                ivToolbarRightIcon.setImageResource(R.drawable.ic_edit)
                ivToolbarRightIcon.visibility = View.VISIBLE

                lDataConditions.visibility = View.VISIBLE
                lNoDataConditions.visibility = View.GONE

                mAdapter?.notifyDataSetChanged()
            } else {
                ivToolbarRightIcon.visibility = View.GONE

                lDataConditions.visibility = View.GONE
                lNoDataConditions.visibility = View.VISIBLE
            }

        })

        hideLoader()
    }

    private fun handleClickListner() {
        ivBack.setOnClickListener {
            onBackPressed()
        }

        ivAddData.setOnClickListener {
            val bundle = Bundle()
            val conditionsBottomSheetFragment = ConditionsBottomSheetFragment.newInstance(bundle)
            conditionsBottomSheetFragment.show(supportFragmentManager, ConditionsBottomSheetFragment.TAG)
        }

        ivToolbarRightIcon.setOnClickListener {
            val bundle = Bundle()
            val conditionsBottomSheetFragment = ConditionsBottomSheetFragment.newInstance(bundle)
            conditionsBottomSheetFragment.show(supportFragmentManager, ConditionsBottomSheetFragment.TAG)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onAddConditions() {
        val intent = Intent(this@ConditionsActivity, AddConditionsActivity::class.java)
        intent.putExtra(Const.IS_EDIT_CONDITIONS, false)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onConditionsImageClick(pictureUri: String) {
        val intent = Intent(this@ConditionsActivity, FullScreenImageVideoActivity::class.java)
        intent.putExtra(Const.PICTURE_URI, pictureUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onConditionsVideoClick(pictureUri: String, videoUri: String) {
        val intent = Intent(this@ConditionsActivity, FullScreenImageVideoActivity::class.java)
        intent.putExtra(Const.VIDEO_URI, videoUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onItemClick(conditions: Conditions) {
        mConditions = conditions
        val bundle = Bundle()
        val conditionBottomSheetFragment = ConditionBottomSheetFragment.newInstance(bundle)
        conditionBottomSheetFragment.show(supportFragmentManager, ConditionBottomSheetFragment.TAG)
    }

    override fun onEditData() {
        val intent = Intent(this@ConditionsActivity, AddConditionsActivity::class.java)
        intent.putExtra(Const.IS_EDIT_CONDITIONS, true)
        intent.putExtra(Const.ID_CONDITIONS, mConditions.conditionsId)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onDeleteData() {
        showDeleteDialog()
    }

    private fun showDeleteDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.item_alert_dialog, null)
        val mBuilder = AlertDialog.Builder(this, R.style.CustomBottomSheetDialogTheme).setView(mDialogView).setCancelable(false)
        val mAlertDialog = mBuilder.show()
        mDialogView.tvDialogCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
        mDialogView.tvDialogDelete.setOnClickListener {
            deleteConditionsFromDB()
            mAlertDialog.dismiss()
        }
    }

    private fun deleteConditionsFromDB() {
        showLoader()

        val thread = Thread(Runnable {
            conditionsViewModel.deleteConditions(mConditions.conditionsId)
        })
        thread.start()

        hideLoader()
    }
}