package com.app.iiam.summary

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.iiam.R
import com.app.iiam.abilities.AbilitiesAdapter
import com.app.iiam.aboutme.NotesAdapter
import com.app.iiam.base.BaseActivity
import com.app.iiam.conditions.ConditionsAdapter
import com.app.iiam.database.entities.*
import com.app.iiam.database.viewmodel.*
import com.app.iiam.fullscreenimage.FullScreenImageVideoActivity
import com.app.iiam.fullscreenimage.FullScreenImagesActivity
import com.app.iiam.medications.MedicationsAdapter
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.records.RecordsAdapter
import com.app.iiam.utils.Const
import kotlinx.android.synthetic.main.activity_summary.*
import kotlinx.android.synthetic.main.toolbar_home.*

class SummaryActivity : BaseActivity(), NotesAdapter.OnItemClickListener, AbilitiesAdapter.OnItemClickListener,
    MedicationsAdapter.OnItemClickListener, ConditionsAdapter.OnItemClickListener, RecordsAdapter.OnItemClickListener {

    private var mUserId: Long = 0

    private lateinit var userDetailsViewModel: UserDetailsViewModel

    private lateinit var noteViewModel: NoteViewModel
    private var mNotesAdapter: NotesAdapter? = null
    private lateinit var notesLinearLayoutManager: LinearLayoutManager
    private var mNotesList = mutableListOf<Note>()

    private lateinit var abilitiesViewModel: AbilitiesViewModel
    private var mAbilitiesAdapter: AbilitiesAdapter? = null
    private lateinit var abilitiesLinearLayoutManager: LinearLayoutManager
    private var mAbilitiesList = mutableListOf<Abilities>()

    private lateinit var medicationsViewModel: MedicationsViewModel
    private var mMedicationsAdapter: MedicationsAdapter? = null
    private lateinit var medicationsLinearLayoutManager: LinearLayoutManager
    private var mMedicationsList = mutableListOf<Medications>()

    private lateinit var conditionsViewModel: ConditionsViewModel
    private var mConditionsAdapter: ConditionsAdapter? = null
    private lateinit var conditionsLinearLayoutManager: LinearLayoutManager
    private var mConditionsList = mutableListOf<Conditions>()

    private lateinit var recordsViewModel: RecordsViewModel
    private var mRecordsAdapter: RecordsAdapter? = null
    private lateinit var recordsLinearLayoutManager: LinearLayoutManager
    private var mRecordsList = mutableListOf<Records>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        initView()
        handleClickListner()
    }

    private fun initView() {

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.setText(getString(R.string.summary))
        tvToolbarName.visibility = View.VISIBLE

        mUserId = SharedPrefsManager.getLong(Const.USER_ID)

        userDetailsViewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        notesLinearLayoutManager = LinearLayoutManager(this)
        mNotesAdapter = NotesAdapter(mNotesList, this)
        rvNotes.layoutManager = notesLinearLayoutManager
        rvNotes.adapter = mNotesAdapter

        abilitiesViewModel = ViewModelProvider(this).get(AbilitiesViewModel::class.java)
        abilitiesLinearLayoutManager = LinearLayoutManager(this)
        mAbilitiesAdapter = AbilitiesAdapter(mAbilitiesList, this)
        rvAbilities.layoutManager = abilitiesLinearLayoutManager
        rvAbilities.adapter = mAbilitiesAdapter

        medicationsViewModel = ViewModelProvider(this).get(MedicationsViewModel::class.java)
        medicationsLinearLayoutManager = LinearLayoutManager(this)
        mMedicationsAdapter = MedicationsAdapter(mMedicationsList, this)
        rvMedications.layoutManager = medicationsLinearLayoutManager
        rvMedications.adapter = mMedicationsAdapter

        conditionsViewModel = ViewModelProvider(this).get(ConditionsViewModel::class.java)
        conditionsLinearLayoutManager = LinearLayoutManager(this)
        mConditionsAdapter = ConditionsAdapter(mConditionsList, this)
        rvConditions.layoutManager = conditionsLinearLayoutManager
        rvConditions.adapter = mConditionsAdapter

        recordsViewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)
        recordsLinearLayoutManager = LinearLayoutManager(this)
        mRecordsAdapter = RecordsAdapter(mRecordsList, this)
        rvRecords.layoutManager = recordsLinearLayoutManager
        rvRecords.adapter = mRecordsAdapter

        getAllDataFromDB()

    }

    private fun getAllDataFromDB() {
        showLoader()

        userDetailsViewModel.getUserDetails(mUserId).observe(this, Observer { userDetailsViewModel ->
            userDetailsViewModel.let {
                if (userDetailsViewModel != null) {
                    tvName.text = getString(R.string.name) + ": " + it.userName
                    tvPreferredName.text = getString(R.string.preferred_name) + ": " + it.userPreferredName
                    tvAge.text = getString(R.string.age) + ": " + it.userAge
                    tvGender.text = getString(R.string.gender) + ": " + it.userGender
                    tvAllergies.text = getString(R.string.allergies) + ": " + it.userAllergies
                    tvPhoneNumber.text = getString(R.string.phone_number) + ": " + it.userPhoneNumber
                    tvEmail.text = getString(R.string.email) + ": " + SharedPrefsManager.getString(Const.USER_EMAIL)
                    tvEmergencyContact.text = getString(R.string.emergency_contact) + ": " + it.userEmergencyContact
                    tvInfo.text = getString(R.string.more_info) + " " + it.userName + "."
                    llAboutMeBio.visibility = View.VISIBLE
                } else {
                    llAboutMeBio.visibility = View.GONE
                }
            }
        })

        noteViewModel.getAllNotes(mUserId).observe(this, Observer { notes ->
            mNotesList.clear()
            mNotesList.addAll(notes)
            if (mNotesList.size != 0) {
                tvNotes.visibility = View.GONE
                rvNotes.visibility = View.VISIBLE
                mNotesAdapter?.notifyDataSetChanged()
            } else {
                tvNotes.visibility = View.VISIBLE
                rvNotes.visibility = View.GONE
            }
        })

        abilitiesViewModel.getAllAbilities(mUserId).observe(this, Observer { abilities ->
            mAbilitiesList.clear()
            mAbilitiesList.addAll(abilities)
            if (mAbilitiesList.size != 0) {
                tvAbilities.visibility = View.GONE
                rvAbilities.visibility = View.VISIBLE
                mAbilitiesAdapter?.notifyDataSetChanged()
            } else {
                tvAbilities.visibility = View.VISIBLE
                rvAbilities.visibility = View.GONE
            }
        })

        medicationsViewModel.getAllMedications(mUserId).observe(this, Observer { medications ->
            mMedicationsList.clear()
            mMedicationsList.addAll(medications)
            if (mMedicationsList.size != 0) {
                tvMedications.visibility = View.GONE
                rvMedications.visibility = View.VISIBLE
                mMedicationsAdapter?.notifyDataSetChanged()
            } else {
                tvMedications.visibility = View.VISIBLE
                rvMedications.visibility = View.GONE
            }
        })

        conditionsViewModel.getAllConditions(mUserId).observe(this, Observer { conditions ->
            mConditionsList.clear()
            mConditionsList.addAll(conditions)
            if (mConditionsList.size != 0) {
                tvConditions.visibility = View.GONE
                rvConditions.visibility = View.VISIBLE
                mConditionsAdapter?.notifyDataSetChanged()
            } else {
                tvConditions.visibility = View.VISIBLE
                rvConditions.visibility = View.GONE
            }
        })

        recordsViewModel.getAllRecords(mUserId).observe(this, Observer { records ->
            mRecordsList.clear()
            mRecordsList.addAll(records)
            if (mRecordsList.size != 0) {
                tvRecords.visibility = View.GONE
                rvRecords.visibility = View.VISIBLE
                mRecordsAdapter?.notifyDataSetChanged()
            } else {
                tvRecords.visibility = View.VISIBLE
                rvRecords.visibility = View.GONE
            }
        })

        hideLoader()
    }

    private fun handleClickListner() {
        ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onItemClick(note: Note) {
    }

    override fun onNotesImageClick(pictureUri: String) {
        goToFullScreenImage(pictureUri)
    }

    override fun onNotesVideoClick(pictureUri: String, videoUri: String) {
        goToFullScreenVideo(videoUri)
    }

    override fun onItemClick(abilities: Abilities) {
    }

    override fun onAbilitiesImageClick(pictureUri: String) {
        goToFullScreenImage(pictureUri)
    }

    override fun onAbilitiesVideoClick(pictureUri: String, videoUri: String) {
        goToFullScreenVideo(videoUri)
    }

    override fun onItemClick(medications: Medications) {
    }

    override fun onMedicationsImageClick(pictureUri: String) {
        goToFullScreenImage(pictureUri)
    }

    override fun onItemClick(conditions: Conditions) {
    }

    override fun onConditionsImageClick(pictureUri: String) {
        goToFullScreenImage(pictureUri)
    }

    override fun onConditionsVideoClick(pictureUri: String, videoUri: String) {
        goToFullScreenVideo(videoUri)
    }

    override fun onItemClick(records: Records) {
    }

    override fun onRecordsImageClick(pictureUri: String) {
        val intent = Intent(this, FullScreenImagesActivity::class.java)
        intent.putExtra(Const.PICTURE_URI, pictureUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun goToFullScreenImage(pictureUri: String) {
        val intent = Intent(this, FullScreenImageVideoActivity::class.java)
        intent.putExtra(Const.PICTURE_URI, pictureUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun goToFullScreenVideo(videoUri: String) {
        val intent = Intent(this, FullScreenImageVideoActivity::class.java)
        intent.putExtra(Const.VIDEO_URI, videoUri)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}