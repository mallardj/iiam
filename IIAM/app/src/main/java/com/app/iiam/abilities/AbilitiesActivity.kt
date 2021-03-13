package com.app.iiam.abilities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.iiam.R
import com.app.iiam.addabilities.AddAbilitiesActivity
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.Abilities
import com.app.iiam.database.viewmodel.AbilitiesViewModel
import com.app.iiam.fullscreenimage.FullScreenImageVideoActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import kotlinx.android.synthetic.main.activity_abilities.*
import kotlinx.android.synthetic.main.item_alert_dialog.view.*
import kotlinx.android.synthetic.main.toolbar_home.*

class AbilitiesActivity : BaseActivity(), AbilitiesBottomSheetFragment.ItemClickListener, AbilitiesAdapter.OnItemClickListener, AbilityBottomSheetFragment.ItemClickListener {

    private lateinit var abilitiesViewModel: AbilitiesViewModel
    private var mAdapter: AbilitiesAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mAbilitiesList = mutableListOf<Abilities>()
    private lateinit var mAbilities: Abilities

    override fun onStart() {
        super.onStart()

        getAbilityDetailsFromDB()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abilities)

        initView()
        handleClickListner()
    }

    private fun initView() {

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.setText(getString(R.string.abilities))
        tvToolbarName.visibility = View.VISIBLE

        linearLayoutManager = LinearLayoutManager(this@AbilitiesActivity)
        mAdapter = AbilitiesAdapter(mAbilitiesList, this)
        rvAbilities.layoutManager = linearLayoutManager
        rvAbilities.adapter = mAdapter

    }

    private fun getAbilityDetailsFromDB() {
        showLoader()

        val mUserId = SharedPrefsManager.getLong(Const.USER_ID)

        abilitiesViewModel = ViewModelProvider(this).get(AbilitiesViewModel::class.java)

        abilitiesViewModel.getAllAbilities(mUserId).observe(this, Observer { abilities ->

            mAbilitiesList.clear()
            mAbilitiesList.addAll(abilities)

            if (mAbilitiesList.size != 0) {
                ivToolbarRightIcon.setImageResource(R.drawable.ic_edit)
                ivToolbarRightIcon.visibility = View.VISIBLE

                lDataAbilities.visibility = View.VISIBLE
                lNoDataAbilities.visibility = View.GONE

                mAdapter?.notifyDataSetChanged()
            } else {
                ivToolbarRightIcon.visibility = View.GONE

                lDataAbilities.visibility = View.GONE
                lNoDataAbilities.visibility = View.VISIBLE
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
            val abilitiesBottomSheetFragment = AbilitiesBottomSheetFragment.newInstance(bundle)
            abilitiesBottomSheetFragment.show(supportFragmentManager, AbilitiesBottomSheetFragment.TAG)
        }

        ivToolbarRightIcon.setOnClickListener {
            val bundle = Bundle()
            val abilitiesBottomSheetFragment = AbilitiesBottomSheetFragment.newInstance(bundle)
            abilitiesBottomSheetFragment.show(supportFragmentManager, AbilitiesBottomSheetFragment.TAG)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onAddAbilities() {
        val intent = Intent(this@AbilitiesActivity, AddAbilitiesActivity::class.java)
        intent.putExtra(Const.IS_EDIT_ABILITY, false)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onAbilitiesImageClick(pictureUri: String) {
        val intent = Intent(this@AbilitiesActivity, FullScreenImageVideoActivity::class.java)
        intent.putExtra(Const.PICTURE_URI, pictureUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onAbilitiesVideoClick(pictureUri: String, videoUri: String) {
        val intent = Intent(this@AbilitiesActivity, FullScreenImageVideoActivity::class.java)
        intent.putExtra(Const.VIDEO_URI, videoUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onItemClick(abilities: Abilities) {
        mAbilities = abilities
        val bundle = Bundle()
        val abilityBottomSheetFragment = AbilityBottomSheetFragment.newInstance(bundle)
        abilityBottomSheetFragment.show(supportFragmentManager, AbilityBottomSheetFragment.TAG)
    }

    override fun onEditData() {
        val intent = Intent(this@AbilitiesActivity, AddAbilitiesActivity::class.java)
        intent.putExtra(Const.IS_EDIT_ABILITY, true)
        intent.putExtra(Const.ID_ABILITY, mAbilities.abilitiesId)
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
            deleteAbilityFromDB()
            mAlertDialog.dismiss()
        }
    }

    private fun deleteAbilityFromDB() {
        showLoader()

        val thread = Thread(Runnable {
            abilitiesViewModel.deleteAbilities(mAbilities.abilitiesId)
        })
        thread.start()

        hideLoader()
    }

}