package com.app.iiam.aboutme

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.iiam.R
import com.app.iiam.addnote.AddNoteActivity
import com.app.iiam.base.BaseActivity
import com.app.iiam.biographicinformation.BiographicInformationActivity
import com.app.iiam.database.entities.Note
import com.app.iiam.database.entities.UserDetails
import com.app.iiam.database.viewmodel.NoteViewModel
import com.app.iiam.database.viewmodel.UserDetailsViewModel
import com.app.iiam.fullscreenimage.FullScreenImageVideoActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_about_me.*
import kotlinx.android.synthetic.main.item_alert_dialog.view.*
import kotlinx.android.synthetic.main.toolbar_home.*

class AboutMeActivity : BaseActivity(), AboutMeBottomSheetFragment.ItemClickListener, NotesAdapter.OnItemClickListener, NoteBottomSheetFragment.ItemClickListener {

    private lateinit var userDetailsViewModel: UserDetailsViewModel
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var mUserDetails: UserDetails
    private var mUserId: Long = 0
    private var mAdapter: NotesAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mNotesList = mutableListOf<Note>()
    private lateinit var mNote: Note
    private var isBioData = false

    companion object {
        private const val TAG = "AboutMeActivity"
    }

    override fun onStart() {
        super.onStart()

        getUserDetailsFromDB()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        initView()
        handleClickListner()
    }

    private fun initView() {

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.setText(getString(R.string.about_me))
        tvToolbarName.visibility = View.VISIBLE

        linearLayoutManager = LinearLayoutManager(this)
        mAdapter = NotesAdapter(mNotesList, this)
        rvNotes.layoutManager = linearLayoutManager
        rvNotes.adapter = mAdapter

    }

    private fun getUserDetailsFromDB() {
        showLoader()

        mUserId = SharedPrefsManager.getLong(Const.USER_ID)

        // get biographic info
        userDetailsViewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)

        userDetailsViewModel.getUserDetails(mUserId).observe(this, Observer { userDetailsViewModel ->
            userDetailsViewModel.let {
                if (userDetailsViewModel != null) {
                    ivToolbarRightIcon.setImageResource(R.drawable.ic_edit)
                    ivToolbarRightIcon.visibility = View.VISIBLE

                    lDataAboutMe.visibility = View.VISIBLE
                    llAboutMeBio.visibility = View.VISIBLE
                    lNoDataAboutMe.visibility = View.GONE

                    mUserDetails = userDetailsViewModel
                    isBioData = true

                    tvMeName.text = it.userName
                    tvMeAge.text = it.userAge
                    tvMeGender.text = it.userGender
                    tvMeAllergies.text = it.userAllergies
                    Glide.with(this)
                        .asBitmap()
                        .load(it.userProfilePicture)
                        .placeholder(R.drawable.ic_dummy_image)
                        .circleCrop()
                        .into(tvMePhoto)
                } else {
                    llAboutMeBio.visibility = View.GONE
                    isBioData = false
                }
            }
        })

        // get notes
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        noteViewModel.getAllNotes(mUserId).observe(this, Observer { notes ->

            mNotesList.clear()
            mNotesList.addAll(notes)

            if (mNotesList.size != 0) {
                ivToolbarRightIcon.setImageResource(R.drawable.ic_edit)
                ivToolbarRightIcon.visibility = View.VISIBLE

                lDataAboutMe.visibility = View.VISIBLE
                rvNotes.visibility = View.VISIBLE
                tvNotesTitle.visibility = View.VISIBLE
                lNoDataAboutMe.visibility = View.GONE

                mAdapter?.notifyDataSetChanged()
            } else {
                rvNotes.visibility = View.GONE
                tvNotesTitle.visibility = View.GONE
                if (!isBioData) {
                    ivToolbarRightIcon.visibility = View.GONE
                    llAboutMeBio.visibility = View.GONE
                    lDataAboutMe.visibility = View.GONE
                }
                lNoDataAboutMe.visibility = View.VISIBLE

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
            val aboutMeBottomSheetFragment = AboutMeBottomSheetFragment.newInstance(bundle, getString(R.string.add_biographic_information))
            aboutMeBottomSheetFragment.show(supportFragmentManager, AboutMeBottomSheetFragment.TAG)
        }

        ivToolbarRightIcon.setOnClickListener {
            if (isBioData) {
                val bundle = Bundle()
                val aboutMeBottomSheetFragment = AboutMeBottomSheetFragment.newInstance(bundle, getString(R.string.edit_biographic_information))
                aboutMeBottomSheetFragment.show(supportFragmentManager, AboutMeBottomSheetFragment.TAG)
            } else {
                val bundle = Bundle()
                val aboutMeBottomSheetFragment = AboutMeBottomSheetFragment.newInstance(bundle, getString(R.string.add_biographic_information))
                aboutMeBottomSheetFragment.show(supportFragmentManager, AboutMeBottomSheetFragment.TAG)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onAddBiographicInformation() {
        val intent = Intent(this, BiographicInformationActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onAddNote() {
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra(Const.IS_EDIT_NOTE, false)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onNotesImageClick(pictureUri: String) {
        val intent = Intent(this, FullScreenImageVideoActivity::class.java)
        intent.putExtra(Const.PICTURE_URI, pictureUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onNotesVideoClick(pictureUri: String, videoUri: String) {
        val intent = Intent(this, FullScreenImageVideoActivity::class.java)
        intent.putExtra(Const.VIDEO_URI, videoUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onItemClick(note: Note) {
        mNote = note
        val bundle = Bundle()
        val noteBottomSheetFragment = NoteBottomSheetFragment.newInstance(bundle)
        noteBottomSheetFragment.show(supportFragmentManager, NoteBottomSheetFragment.TAG)
    }

    override fun onEditNote() {
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra(Const.IS_EDIT_NOTE, true)
        intent.putExtra(Const.ID_NOTE, mNote.noteId)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onDeleteNote() {
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
            deleteNoteFromDB()
            mAlertDialog.dismiss()
        }
    }

    private fun deleteNoteFromDB() {
        showLoader()

        val thread = Thread(Runnable {
            noteViewModel.deleteNote(mNote.noteId)
        })
        thread.start()

        hideLoader()
    }

}