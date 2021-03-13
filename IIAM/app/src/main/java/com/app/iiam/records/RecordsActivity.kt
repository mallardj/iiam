package com.app.iiam.records

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.iiam.R
import com.app.iiam.addrecord.AddRecordActivity
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.Records
import com.app.iiam.database.viewmodel.RecordsViewModel
import com.app.iiam.fullscreenimage.FullScreenImagesActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import kotlinx.android.synthetic.main.activity_records.*
import kotlinx.android.synthetic.main.item_alert_dialog.view.*
import kotlinx.android.synthetic.main.toolbar_home.*

class RecordsActivity : BaseActivity(), RecordsBottomSheetFragment.ItemClickListener, RecordsAdapter.OnItemClickListener, RecordBottomSheetFragment.ItemClickListener {

    private lateinit var recordsViewModel: RecordsViewModel
    private var mAdapter: RecordsAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mRecordsList = mutableListOf<Records>()
    private lateinit var mRecords: Records

    override fun onStart() {
        super.onStart()

        getRecordsDetailsFromDB()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        initView()
        handleClickListner()
    }

    private fun initView() {

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.setText(getString(R.string.records))
        tvToolbarName.visibility = View.VISIBLE

        linearLayoutManager = LinearLayoutManager(this@RecordsActivity)
        mAdapter = RecordsAdapter(mRecordsList, this)
        rvRecords.layoutManager = linearLayoutManager
        rvRecords.adapter = mAdapter

    }

    private fun getRecordsDetailsFromDB() {
        showLoader()

        val mUserId = SharedPrefsManager.getLong(Const.USER_ID)

        recordsViewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)

        recordsViewModel.getAllRecords(mUserId).observe(this, Observer { records ->

            mRecordsList.clear()
            mRecordsList.addAll(records)

            if (mRecordsList.size != 0) {
                ivToolbarRightIcon.setImageResource(R.drawable.ic_edit)
                ivToolbarRightIcon.visibility = View.VISIBLE

                lDataRecords.visibility = View.VISIBLE
                lNoDataRecords.visibility = View.GONE

                mAdapter?.notifyDataSetChanged()
            } else {
                ivToolbarRightIcon.visibility = View.GONE

                lDataRecords.visibility = View.GONE
                lNoDataRecords.visibility = View.VISIBLE
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
            val recordsBottomSheetFragment = RecordsBottomSheetFragment.newInstance(bundle)
            recordsBottomSheetFragment.show(supportFragmentManager, RecordsBottomSheetFragment.TAG)
        }

        ivToolbarRightIcon.setOnClickListener {
            val bundle = Bundle()
            val recordsBottomSheetFragment = RecordsBottomSheetFragment.newInstance(bundle)
            recordsBottomSheetFragment.show(supportFragmentManager, RecordsBottomSheetFragment.TAG)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onAddPictureOfRecord() {
        val intent = Intent(this@RecordsActivity, AddRecordActivity::class.java)
        intent.putExtra(Const.IS_EDIT_RECORDS, false)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onRecordsImageClick(pictureUri: String) {
        val intent = Intent(this@RecordsActivity, FullScreenImagesActivity::class.java)
        intent.putExtra(Const.PICTURE_URI, pictureUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onItemClick(records: Records) {
        mRecords = records
        val bundle = Bundle()
        val recordBottomSheetFragment = RecordBottomSheetFragment.newInstance(bundle)
        recordBottomSheetFragment.show(supportFragmentManager, RecordBottomSheetFragment.TAG)
    }

    override fun onEditData() {
        val intent = Intent(this@RecordsActivity, AddRecordActivity::class.java)
        intent.putExtra(Const.IS_EDIT_RECORDS, true)
        intent.putExtra(Const.ID_RECORDS, mRecords.recordsId)
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
            deleteRecordsFromDB()
            mAlertDialog.dismiss()
        }
    }

    private fun deleteRecordsFromDB() {
        showLoader()

        val thread = Thread(Runnable {
            recordsViewModel.deleteRecords(mRecords.recordsId)
        })
        thread.start()

        hideLoader()
    }
}