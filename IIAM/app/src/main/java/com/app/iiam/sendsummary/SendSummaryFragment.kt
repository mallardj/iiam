package com.app.iiam.sendsummary

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.IIAM.Companion.getAppContext
import com.app.iiam.R
import com.app.iiam.base.BaseFragment
import com.app.iiam.database.entities.*
import com.app.iiam.database.viewmodel.*
import com.app.iiam.models.PdfImagesModel
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.*
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.fragment_send_summary.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class SendSummaryFragment : BaseFragment() {

    private var isSendMedicationsSummary: Boolean? = false
    private var mUserId: Long = 0
    private var mFinalText = ""
    private var mImagesList = mutableListOf<PdfImagesModel>()

    private var mUserDetails = ""
    private lateinit var userDetailsViewModel: UserDetailsViewModel

    private var mNotes = ""
    private lateinit var noteViewModel: NoteViewModel
    private var mNotesList = mutableListOf<Note>()

    private var mAbilities = ""
    private lateinit var abilitiesViewModel: AbilitiesViewModel
    private var mAbilitiesList = mutableListOf<Abilities>()

    private var mMedications = ""
    private lateinit var medicationsViewModel: MedicationsViewModel
    private var mMedicationsList = mutableListOf<Medications>()

    private var mConditions = ""
    private lateinit var conditionsViewModel: ConditionsViewModel
    private var mConditionsList = mutableListOf<Conditions>()

    private var mRecords = ""
    private lateinit var recordsViewModel: RecordsViewModel
    private var mRecordsList = mutableListOf<Records>()

    private lateinit var mPermissionUtils: PermissionUtils
    private lateinit var mFileUtils: FileUtils
    private lateinit var pdfFolder: File
    private lateinit var myPDF: File

    companion object {
        private const val TAG = "SendSummaryFragment"
        const val PERMISSIONS_STORAGE = 8976
        fun newInstance(context: Context, isSendMedicationsSummary: Boolean): SendSummaryFragment {
            val bundle = Bundle()
            bundle.putBoolean(Const.SUMMARY, isSendMedicationsSummary)
            val fragment = SendSummaryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_send_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleClickListner()
    }

    private fun initView() {

        val bundle = arguments
        isSendMedicationsSummary = bundle?.getBoolean(Const.SUMMARY, false)

        mUserId = SharedPrefsManager.getLong(Const.USER_ID)

        mPermissionUtils = PermissionUtils()
        mFileUtils = FileUtils(activity!!)
    }

    @SuppressLint("DefaultLocale")
    private fun handleClickListner() {

        btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(etRecipientEmail.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.recipient_email).toLowerCase()
                    )
                )
            } else if (isEmailInvalid(etRecipientEmail.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_invalid_field),
                        getString(R.string.recipient_email).toLowerCase()
                    )
                )
            } else if (TextUtils.isEmpty(etConfirmEmail.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.confirm_email).toLowerCase()
                    )
                )
            } else if (isEmailInvalid(etConfirmEmail.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_invalid_field),
                        getString(R.string.confirm_email).toLowerCase()
                    )
                )
            } else if (etRecipientEmail.text.toString().trim() != etConfirmEmail.text.toString().trim()) {
                showError(getString(R.string.validation_msg_email))
            } else {
                val isPermissionAllowed = mPermissionUtils.checkMissingPermission(
                    this, PERMISSIONS_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (isPermissionAllowed) {
                    proceedToSendEmail()
                }
            }
        }

    }

    private fun proceedToSendEmail() {
        if (isSendMedicationsSummary == true) {
            getMedicationDataFromDB()
        } else {
            getAllDataFromDB()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_STORAGE) {
            val userAllowedCameraPermission = mPermissionUtils.checkGrantResults(grantResults)
            if (userAllowedCameraPermission) {
                proceedToSendEmail()
            } else {
                showError(getString(R.string.storage_permission_required))
            }
        }
    }

    private fun getAllDataFromDB() {
        showLoader()

        mImagesList.clear()
        mFinalText = ""
        mUserDetails = ""
        mNotes = ""
        mAbilities = ""
        mMedications = ""
        mConditions = ""
        mRecords = ""

        userDetailsViewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)
        userDetailsViewModel.getUserDetails(mUserId).removeObservers(this)
        userDetailsViewModel.getUserDetails(mUserId).observe(getViewLifecycleOwner(), Observer { userDetailsViewModel ->
            userDetailsViewModel.let {
                if (userDetailsViewModel != null) {
                    if (!TextUtils.isEmpty(userDetailsViewModel.userProfilePicture)) {
                        mImagesList.add(PdfImagesModel(getString(R.string.profile_picture), userDetailsViewModel.userProfilePicture.toString()))
                    }
                    mUserDetails += getString(R.string.personal_info) + "\n"
                    mUserDetails += getString(R.string.name) + ": " + it.userName + "\n"
                    mUserDetails += getString(R.string.preferred_name) + ": " + it.userPreferredName + "\n"
                    mUserDetails += getString(R.string.age) + ": " + it.userAge + "\n"
                    mUserDetails += getString(R.string.gender) + ": " + it.userGender + "\n"
                    mUserDetails += getString(R.string.allergies) + ": " + it.userAllergies + "\n"
                    mUserDetails += getString(R.string.phone_number) + ": " + it.userPhoneNumber + "\n"
                    mUserDetails += getString(R.string.email) + ": " + SharedPrefsManager.getString(Const.USER_EMAIL) + "\n"
                    mUserDetails += getString(R.string.emergency_contact) + ": " + it.userEmergencyContact + "\n" + "\n"
                } else {
                    mUserDetails = getString(R.string.no_personal_info_available) + "\n" + "\n"
                }
            }

            noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
            noteViewModel.getAllNotes(mUserId).removeObservers(this)
            noteViewModel.getAllNotes(mUserId).observe(getViewLifecycleOwner(), Observer { notes ->
                mNotesList.clear()
                mNotesList.addAll(notes)
                if (mNotesList.size != 0) {
                    for (i in 0 until mNotesList.size) {
                        if (mNotesList[i].noteMediaType.equals(getString(R.string.image))) {
                            if (!TextUtils.isEmpty(mNotesList[i].notePicture)) {
                                mImagesList.add(PdfImagesModel((getString(R.string.notes) + " - " + mNotesList[i].noteTitle), mNotesList[i].notePicture.toString()))
                            }
                        }
                        mNotes += getString(R.string.notes) + "\n"
                        mNotes += getString(R.string.title) + ": " + mNotesList[i].noteTitle + "\n"
                        mNotes += getString(R.string.comments) + ": " + mNotesList[i].noteComment + "\n"
                        mNotes += getString(R.string.date) + ": " + mNotesList[i].noteDate.toString().setDate(mNotesList[i].noteDate!!, NOTE_FORMATTED_DATE) + "\n" + "\n"
                    }
                } else {
                    mNotes = getString(R.string.no_notes_data_available) + "\n" + "\n"
                }

                abilitiesViewModel = ViewModelProvider(this).get(AbilitiesViewModel::class.java)
                abilitiesViewModel.getAllAbilities(mUserId).removeObservers(this)
                abilitiesViewModel.getAllAbilities(mUserId).observe(getViewLifecycleOwner(), Observer { abilities ->
                    mAbilitiesList.clear()
                    mAbilitiesList.addAll(abilities)
                    if (mAbilitiesList.size != 0) {
                        for (i in 0 until mAbilitiesList.size) {
                            if (mAbilitiesList[i].abilitiesMediaType.equals(getString(R.string.image))) {
                                if (!TextUtils.isEmpty(mAbilitiesList[i].abilitiesPicture)) {
                                    mImagesList.add(PdfImagesModel((getString(R.string.abilities) + " - " + mAbilitiesList[i].abilitiesAbility), mAbilitiesList[i].abilitiesPicture.toString()))
                                }
                            }
                            mAbilities += getString(R.string.abilities) + "\n"
                            mAbilities += getString(R.string.ability) + ": " + mAbilitiesList[i].abilitiesAbility + "\n"
                            mAbilities += getString(R.string.task) + ": " + mAbilitiesList[i].abilitiesTask + "\n"
                            mAbilities += getString(R.string.comments) + ": " + mAbilitiesList[i].abilitiesComment + "\n" + "\n"
                        }
                    } else {
                        mAbilities = getString(R.string.no_abilities) + "\n" + "\n"
                    }

                    medicationsViewModel = ViewModelProvider(this).get(MedicationsViewModel::class.java)
                    medicationsViewModel.getAllMedications(mUserId).removeObservers(this)
                    medicationsViewModel.getAllMedications(mUserId).observe(getViewLifecycleOwner(), Observer { medications ->
                        mMedicationsList.clear()
                        mMedicationsList.addAll(medications)
                        if (mMedicationsList.size != 0) {
                            for (i in 0 until mMedicationsList.size) {
                                if (!TextUtils.isEmpty(mMedicationsList[i].medicationsPicture)) {
                                    mImagesList.add(PdfImagesModel((getString(R.string.medications) + " - " + mMedicationsList[i].medicationsName), mMedicationsList[i].medicationsPicture.toString()))
                                }
                                mMedications += getString(R.string.medications) + "\n"
                                mMedications += getString(R.string.title) + ": " + mMedicationsList[i].medicationsName + "\n"
                                mMedications += getString(R.string.dose) + ": " + mMedicationsList[i].medicationsDose + "\n"
                                mMedications += getString(R.string.dose_unit) + ": " + mMedicationsList[i].medicationsDoseUnit + "\n"
                                mMedications += getString(R.string.additional_dose_info) + ": " + mMedicationsList[i].medicationsDoseInfo + "\n"
                                mMedications += getString(R.string.route) + ": " + mMedicationsList[i].medicationsRoute + "\n"
                                mMedications += getString(R.string.additional_route_info) + ": " + mMedicationsList[i].medicationsRouteInfo + "\n"
                                mMedications += getString(R.string.frequency) + ": " + mMedicationsList[i].medicationsFrequency + "\n" + "\n"
                            }
                        } else {
                            mMedications = getString(R.string.no_medications) + "\n" + "\n"
                        }

                        conditionsViewModel = ViewModelProvider(this).get(ConditionsViewModel::class.java)
                        conditionsViewModel.getAllConditions(mUserId).removeObservers(this)
                        conditionsViewModel.getAllConditions(mUserId).observe(getViewLifecycleOwner(), Observer { conditions ->
                            mConditionsList.clear()
                            mConditionsList.addAll(conditions)
                            if (mConditionsList.size != 0) {
                                for (i in 0 until mConditionsList.size) {
                                    if (mConditionsList[i].conditionsMediaType.equals(getString(R.string.image))) {
                                        if (!TextUtils.isEmpty(mConditionsList[i].conditionsPicture)) {
                                            mImagesList.add(
                                                PdfImagesModel(
                                                    (getString(R.string.conditions) + " - " + mConditionsList[i].conditionsName),
                                                    mConditionsList[i].conditionsPicture.toString()
                                                )
                                            )
                                        }
                                    }
                                    mConditions += getString(R.string.conditions) + "\n"
                                    mConditions += getString(R.string.title) + ": " + mConditionsList[i].conditionsName + "\n"
                                    mConditions += getString(R.string.symptoms) + ": " + mConditionsList[i].conditionsSymptoms + "\n"
                                    mConditions += getString(R.string.management) + ": " + mConditionsList[i].conditionsManagement + "\n"
                                    mConditions += getString(R.string.additional_comments) + ": " + mConditionsList[i].conditionsAdditionalComments + "\n" + "\n"
                                }
                            } else {
                                mConditions = getString(R.string.no_conditions) + "\n" + "\n"
                            }

                            recordsViewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)
                            recordsViewModel.getAllRecords(mUserId).removeObservers(this)
                            recordsViewModel.getAllRecords(mUserId).observe(getViewLifecycleOwner(), Observer { records ->
                                mRecordsList.clear()
                                mRecordsList.addAll(records)
                                if (mRecordsList.size != 0) {
                                    for (i in 0 until mRecordsList.size) {

                                        if (!TextUtils.isEmpty(mRecordsList[i].recordsPicture)) {
                                            val mediaList: MutableList<String> = mRecordsList[i].recordsPicture!!.split(",").toMutableList()
                                            for (k in 0 until mediaList.size) {
                                                mImagesList.add(PdfImagesModel((getString(R.string.records) + " - " + mRecordsList[i].recordsDocumentsName), mediaList[k]))
                                            }
                                        }

                                        mRecords += getString(R.string.records) + "\n"
                                        mRecords += getString(R.string.document_name) + ": " + mRecordsList[i].recordsDocumentsName + "\n"
                                        mRecords += getString(R.string.pages_in_document) + ": " + mRecordsList[i].recordsPagesInDocument + "\n"
                                        mRecords += getString(R.string.date) + ": " + mRecordsList[i].recordsDate.toString().setDate(mRecordsList[i].recordsDate!!, NOTE_FORMATTED_DATE) + "\n" + "\n"
                                    }
                                } else {
                                    mRecords = getString(R.string.no_records_available) + "\n" + "\n"
                                }

                                mFinalText = mUserDetails + mNotes + mAbilities + mMedications + mConditions + mRecords
                                createPdf()

                            })

                        })

                    })

                })

            })

        })

        /*Handler().postDelayed(Runnable {
            mFinalText = mUserDetails + mNotes + mAbilities + mMedications + mConditions + mRecords
            createPdf()
        }, 4000)*/
    }

    private fun getMedicationDataFromDB() {
        showLoader()

        mImagesList.clear()
        mFinalText = ""
        mMedications = ""

        medicationsViewModel = ViewModelProvider(this).get(MedicationsViewModel::class.java)
        medicationsViewModel.getAllMedications(mUserId).removeObservers(this)
        medicationsViewModel.getAllMedications(mUserId).observe(getViewLifecycleOwner(), Observer { medications ->
            mMedicationsList.clear()
            mMedicationsList.addAll(medications)
            if (mMedicationsList.size != 0) {
                for (i in 0 until mMedicationsList.size) {
                    if (!TextUtils.isEmpty(mMedicationsList[i].medicationsPicture)) {
                        mImagesList.add(PdfImagesModel((getString(R.string.medications) + " - " + mMedicationsList[i].medicationsName), mMedicationsList[i].medicationsPicture.toString()))
                    }
                    mMedications += getString(R.string.medications) + "\n"
                    mMedications += getString(R.string.title) + ": " + mMedicationsList[i].medicationsName + "\n"
                    mMedications += getString(R.string.dose) + ": " + mMedicationsList[i].medicationsDose + "\n"
                    mMedications += getString(R.string.dose_unit) + ": " + mMedicationsList[i].medicationsDoseUnit + "\n"
                    mMedications += getString(R.string.additional_dose_info) + ": " + mMedicationsList[i].medicationsDoseInfo + "\n"
                    mMedications += getString(R.string.route) + ": " + mMedicationsList[i].medicationsRoute + "\n"
                    mMedications += getString(R.string.additional_route_info) + ": " + mMedicationsList[i].medicationsRouteInfo + "\n"
                    mMedications += getString(R.string.frequency) + ": " + mMedicationsList[i].medicationsFrequency + "\n" + "\n"
                }
            } else {
                mMedications = getString(R.string.no_medications) + "\n" + "\n"
            }

            mFinalText = mMedications
            createPdf()
        })

        /*Handler().postDelayed(Runnable {
            mFinalText = mMedications
            createPdf()
        }, 2000)*/
    }

    // function that converts image data into a pdf file
    @Throws(DocumentException::class, IOException::class)
    fun createPdf() {
        try {
            // creates folder with a pathname including the android storage directory
            pdfFolder = File(Environment.getExternalStorageDirectory(), "IIAM")

            // if the directory doesn't already exist, create it
            if (!pdfFolder.exists()) {
                pdfFolder.mkdirs()
                Log.i(TAG, "Folder successfully created")
            }

            // as long as we have images in the recycle view...
            if (mImagesList.size != 0) {

                // progress.setVisibility(View.VISIBLE);

                // name the pdf with the current timestamp by default
                val date = Date()
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(date)
                myPDF = File(pdfFolder.toString() + "/" + timeStamp + ".pdf")


                // point an output stream to our created document
                val output: OutputStream = FileOutputStream(myPDF)
                val document: Document

                // create a document with difference page sizes depending on orientation
                document = Document(PageSize.A4, 50f, 50f, 50f, 50f)
                PdfWriter.getInstance(document, output)
                document.open()

                // loop through all the images in the array
                for (i in 0 until mImagesList.size) {
                    val image = Image.getInstance(mFileUtils.getPath(activity, Uri.parse(mImagesList[i].text))) // Change image's name and extension.

                    if (image == null)
                        return
                    val scaler = (document.pageSize.width - document.leftMargin() - document.rightMargin() - 0) / image.width * 100 // 0 means you have no indentation. If you have any, change it.

                    image.scalePercent(scaler)
                    image.alignment = Image.ALIGN_CENTER or Image.ALIGN_TOP

                    document.add(image)
                    val title = Paragraph(mImagesList[i].title)
                    title.alignment = Element.ALIGN_CENTER
                    document.add(title)
                    document.newPage()
                }
                document.close()

                val newFile = File(pdfFolder.toString() + "/" + "IIAMFiles" + ".pdf")
                val result: Boolean = myPDF.renameTo(newFile)
                myPDF = newFile
                Log.w(TAG, "myPDF renamed to: " + myPDF.toString())
                hideLoader()
                sendEmail()
            } else {
                hideLoader()
                sendEmail()
            }
        } catch (e: DocumentException) {
            e.printStackTrace()
            myPDF.delete()
            hideLoader()
            sendEmail()
        } catch (e: IOException) {
            e.printStackTrace()
            myPDF.delete()
            hideLoader()
            sendEmail()
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            getAppContext().packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun sendEmail() {
        if (isAppInstalled("com.google.android.gm")) {
            val intent = Intent()
            intent.setAction(Intent.ACTION_SEND)
            if (isSendMedicationsSummary == true) {
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.medications_summary))
            } else {
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.summary))
            }
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(etRecipientEmail.text.toString().trim()))
            intent.putExtra(Intent.EXTRA_TEXT, mFinalText)
            if (mImagesList.size != 0) {
                val myPdfFile = File(myPDF.getAbsolutePath())
                if (myPdfFile.exists()) {
                    val fileURI = FileProvider.getUriForFile(context!!, "com.app.iiam.provider", myPdfFile)
                    intent.putExtra(Intent.EXTRA_STREAM, fileURI)
                }
            }
            //intent.setType("application/pdf")
            intent.setType("*/*")
            intent.setPackage("com.google.android.gm")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivity(Intent.createChooser(intent, "Send Summary"))
        } else {
            showToast(getString(R.string.gmail_not_installed))
        }
    }

}