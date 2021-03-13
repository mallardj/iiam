package com.app.iiam.medications

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.iiam.R
import com.app.iiam.addmedications.AddMedicationsActivity
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.Medications
import com.app.iiam.database.viewmodel.MedicationsViewModel
import com.app.iiam.fullscreenimage.FullScreenImageVideoActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import kotlinx.android.synthetic.main.activity_medications.*
import kotlinx.android.synthetic.main.item_alert_dialog.view.*
import kotlinx.android.synthetic.main.toolbar_home.*

class MedicationsActivity : BaseActivity(), MedicationsBottomSheetFragment.ItemClickListener, MedicationsAdapter.OnItemClickListener, MedicationBottomSheetFragment.ItemClickListener {

    private lateinit var medicationsViewModel: MedicationsViewModel
    private var mAdapter: MedicationsAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mMedicationsList = mutableListOf<Medications>()
    private lateinit var mMedications: Medications

    override fun onStart() {
        super.onStart()

        getMedicationsDetailsFromDB()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medications)

        initView()
        handleClickListner()
    }

    private fun initView() {

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.setText(getString(R.string.medications))
        tvToolbarName.visibility = View.VISIBLE

        linearLayoutManager = LinearLayoutManager(this@MedicationsActivity)
        mAdapter = MedicationsAdapter(mMedicationsList, this)
        rvMedications.layoutManager = linearLayoutManager
        rvMedications.adapter = mAdapter

    }

    private fun getMedicationsDetailsFromDB() {
        showLoader()

        val mUserId = SharedPrefsManager.getLong(Const.USER_ID)

        medicationsViewModel = ViewModelProvider(this).get(MedicationsViewModel::class.java)

        medicationsViewModel.getAllMedications(mUserId).observe(this, Observer { medications ->

            mMedicationsList.clear()
            mMedicationsList.addAll(medications)

            if (mMedicationsList.size != 0) {
                ivToolbarRightIcon.setImageResource(R.drawable.ic_edit)
                ivToolbarRightIcon.visibility = View.VISIBLE

                lDataMedications.visibility = View.VISIBLE
                lNoDataMedications.visibility = View.GONE

                mAdapter?.notifyDataSetChanged()
            } else {
                ivToolbarRightIcon.visibility = View.GONE

                lDataMedications.visibility = View.GONE
                lNoDataMedications.visibility = View.VISIBLE
            }

            hideLoader()

        })

    }

    private fun handleClickListner() {
        ivBack.setOnClickListener {
            onBackPressed()
        }

        ivAddData.setOnClickListener {
            val bundle = Bundle()
            val medicationsBottomSheetFragment = MedicationsBottomSheetFragment.newInstance(bundle)
            medicationsBottomSheetFragment.show(supportFragmentManager, MedicationsBottomSheetFragment.TAG)
        }

        ivToolbarRightIcon.setOnClickListener {
            val bundle = Bundle()
            val medicationsBottomSheetFragment = MedicationsBottomSheetFragment.newInstance(bundle)
            medicationsBottomSheetFragment.show(supportFragmentManager, MedicationsBottomSheetFragment.TAG)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onAddMedications() {
        val intent = Intent(this@MedicationsActivity, AddMedicationsActivity::class.java)
        intent.putExtra(Const.IS_EDIT_MEDICATIONS, false)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onMedicationsImageClick(pictureUri: String) {
        val intent = Intent(this@MedicationsActivity, FullScreenImageVideoActivity::class.java)
        intent.putExtra(Const.PICTURE_URI, pictureUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onItemClick(medications: Medications) {
        mMedications = medications
        val bundle = Bundle()
        val medicationBottomSheetFragment = MedicationBottomSheetFragment.newInstance(bundle)
        medicationBottomSheetFragment.show(supportFragmentManager, MedicationBottomSheetFragment.TAG)
    }

    override fun onEditData() {
        val intent = Intent(this@MedicationsActivity, AddMedicationsActivity::class.java)
        intent.putExtra(Const.IS_EDIT_MEDICATIONS, true)
        intent.putExtra(Const.ID_MEDICATIONS, mMedications.medicationsId)
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
            deleteMedicationsFromDB()
            mAlertDialog.dismiss()
        }
    }

    private fun deleteMedicationsFromDB() {
        showLoader()

        val thread = Thread(Runnable {
            medicationsViewModel.deleteMedications(mMedications.medicationsId)
        })
        thread.start()

        hideLoader()
    }
}