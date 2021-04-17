package com.app.iiam.addconditions

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.addnote.MediaBottomSheetFragment
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.Conditions
import com.app.iiam.database.viewmodel.ConditionsViewModel
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import com.app.iiam.utils.logInfo
import com.bumptech.glide.Glide
import com.yanzhenjie.album.Action
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import kotlinx.android.synthetic.main.activity_add_conditions.*
import kotlinx.android.synthetic.main.toolbar_home.*
import lv.chi.photopicker.PhotoPickerFragment
import java.util.*

class AddConditionsActivity : BaseActivity(), PhotoPickerFragment.Callback, MediaBottomSheetFragment.ItemClickListener {

    private lateinit var conditionsViewModel: ConditionsViewModel
    private var uri: Uri? = null
    private var isEditCondition = false
    private var mConditionId: Long = 0
    private var mVideo: String? = null
    private var path: String? = null
    private var mMediaType = ""

    companion object {
        private const val TAG = "AddConditionsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_conditions)

        initView()
        handleClickListner()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        conditionsViewModel = ViewModelProvider(this).get(ConditionsViewModel::class.java)

        isEditCondition = intent.getBooleanExtra(Const.IS_EDIT_CONDITIONS, false)

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.visibility = View.VISIBLE

        if (isEditCondition) {
            mConditionId = intent.getLongExtra(Const.ID_CONDITIONS, 0)
            tvToolbarName.setText(getString(R.string.edit_conditions))
            getConditionsDetailsFromDB()
        } else {
            tvToolbarName.setText(getString(R.string.add_conditions))
        }

        etSymptoms.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etSymptoms.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

        etManagement.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etManagement.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

        etAdditionalComments.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etAdditionalComments.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

    }

    private fun getConditionsDetailsFromDB() {
        showLoader()

        conditionsViewModel.getConditions(mConditionId).observe(this, Observer { condition ->
            condition.let {

                if (it.conditionsMediaType.equals(getString(R.string.image))) {
                    mMediaType = getString(R.string.image)
                    uri = Uri.parse(it.conditionsPicture)
                    Glide.with(this)
                        .asBitmap()
                        .load(uri)
                        .placeholder(R.drawable.ic_add_media)
                        .centerCrop()
                        .into(tvAddPicture)

                    ivPlay.visibility = View.GONE
                } else if (it.conditionsMediaType.equals(getString(R.string.video))) {
                    mMediaType = getString(R.string.video)
                    path = it.conditionsPicture
                    mVideo = it.conditionsVideo
                    Glide.with(this)
                        .asBitmap()
                        .load(path)
                        .placeholder(R.drawable.ic_add_media)
                        .centerCrop()
                        .into(tvAddPicture)

                    ivPlay.visibility = View.VISIBLE
                } else {
                    mMediaType = ""
                    ivPlay.visibility = View.GONE
                }

                etCondition.setText(it.conditionsName)
                etCondition.setSelection(etCondition.text.toString().trim().length)

                etSymptoms.setText(it.conditionsSymptoms)

                etManagement.setText(it.conditionsManagement)

                etAdditionalComments.setText(it.conditionsAdditionalComments)
            }
        })

        hideLoader()
    }

    @SuppressLint("DefaultLocale")
    private fun handleClickListner() {
        ivBack.setOnClickListener {
            onBackPressed()
        }

        tvAddPicture.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Const.MEDIA_TYPE, mMediaType)
            bundle.putBoolean(Const.MULTI_MEDIA, true)
            val mediaBottomSheetFragment = MediaBottomSheetFragment.newInstance(bundle)
            mediaBottomSheetFragment.show(supportFragmentManager, MediaBottomSheetFragment.TAG)
        }

        btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(etCondition.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.condition).toLowerCase()
                    )
                )
            } else if (etCondition.text.toString().trim().length < 2) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.condition),
                            resources.getInteger(R.integer.min_name)
                        )
                    )
                )
            } else if (TextUtils.isEmpty(etSymptoms.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.symptoms).toLowerCase()
                    )
                )
            } else if (etSymptoms.text.toString().trim().length < 2) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.symptoms),
                            resources.getInteger(R.integer.min_name)
                        )
                    )
                )
            } else if (TextUtils.isEmpty(etManagement.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.management).toLowerCase()
                    )
                )
            } else if (etManagement.text.toString().trim().length < 2) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.management),
                            resources.getInteger(R.integer.min_name)
                        )
                    )
                )
            } else {
                if (mMediaType.equals(getString(R.string.image))) {
                    showLoader()
                    if (isEditCondition) {
                        val thread = Thread(Runnable {
                            conditionsViewModel.updateConditions(
                                SharedPrefsManager.getLong(Const.USER_ID),
                                mMediaType,
                                if (uri != null) uri.toString() else "",
                                "",
                                etCondition.text.toString().trim(),
                                etSymptoms.text.toString().trim(),
                                etManagement.text.toString().trim(),
                                etAdditionalComments.text.toString(),
                                mConditionId
                            )

                            hideLoader()
                            finish()
                        })
                        thread.start()
                    } else {
                        val conditions = Conditions(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            mMediaType,
                            if (uri != null) uri.toString() else "",
                            "",
                            etCondition.text.toString().trim(),
                            etSymptoms.text.toString().trim(),
                            etManagement.text.toString().trim(),
                            etAdditionalComments.text.toString()
                        )
                        conditionsViewModel.insert(conditions)

                        hideLoader()
                        finish()
                    }
                } else if (mMediaType.equals(getString(R.string.video))) {
                    showLoader()
                    if (isEditCondition) {
                        val thread = Thread(Runnable {
                            conditionsViewModel.updateConditions(
                                SharedPrefsManager.getLong(Const.USER_ID),
                                mMediaType,
                                if (path != null) path.toString() else "",
                                if (mVideo != null) mVideo.toString() else "",
                                etCondition.text.toString().trim(),
                                etSymptoms.text.toString().trim(),
                                etManagement.text.toString().trim(),
                                etAdditionalComments.text.toString(),
                                mConditionId
                            )

                            hideLoader()
                            finish()
                        })
                        thread.start()
                    } else {
                        val conditions = Conditions(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            mMediaType,
                            if (path != null) path.toString() else "",
                            if (mVideo != null) mVideo.toString() else "",
                            etCondition.text.toString().trim(),
                            etSymptoms.text.toString().trim(),
                            etManagement.text.toString().trim(),
                            etAdditionalComments.text.toString()
                        )
                        conditionsViewModel.insert(conditions)

                        hideLoader()
                        finish()
                    }
                } else {
                    showLoader()
                    if (isEditCondition) {
                        val thread = Thread(Runnable {
                            conditionsViewModel.updateConditions(
                                SharedPrefsManager.getLong(Const.USER_ID),
                                mMediaType,
                                "",
                                "",
                                etCondition.text.toString().trim(),
                                etSymptoms.text.toString().trim(),
                                etManagement.text.toString().trim(),
                                etAdditionalComments.text.toString(),
                                mConditionId
                            )

                            hideLoader()
                            finish()
                        })
                        thread.start()
                    } else {
                        val conditions = Conditions(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            mMediaType,
                            "",
                            "",
                            etCondition.text.toString().trim(),
                            etSymptoms.text.toString().trim(),
                            etManagement.text.toString().trim(),
                            etAdditionalComments.text.toString()
                        )
                        conditionsViewModel.insert(conditions)

                        hideLoader()
                        finish()
                    }
                }
            }
        }

    }

    override fun onSelectImage() {
        openImagePicker()
    }

    override fun onSelectVideo() {
        openVideoPicker()
    }

    override fun onRemoveMedia() {
        removeMedia()
    }

    private fun openImagePicker() {
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = true,
            maxSelection = 1,
            theme = R.style.ChiliPhotoPicker_Dark
        ).show(supportFragmentManager, "picker")
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        logInfo(TAG, photos.joinToString(separator = "\n") { it.toString() })
        uri = photos[0]
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.ic_add_media)
            .centerCrop()
            .into(tvAddPicture)

        mMediaType = getString(R.string.image)
        ivPlay.visibility = View.GONE
    }

    @SuppressLint("NewApi")
    private fun openVideoPicker() {
        Album.video(this) // Video selection.
            .multipleChoice()
            .camera(true)
            .columnCount(3)
            .selectCount(1)
            .widget(
                Widget.newDarkBuilder(this)
                    .title("Select Video")
                    .statusBarColor(getColor(R.color.colorPrimary))
                    .toolBarColor(getColor(R.color.colorPrimary))
                    .mediaItemCheckSelector(getColor(R.color.colorWhite), getColor(R.color.colorLight))
                    .bucketItemCheckSelector(getColor(R.color.colorLight), getColor(R.color.colorPrimary))
                    .build()
            )
            .onResult(object : Action<ArrayList<AlbumFile?>?> {
                override fun onAction(result: ArrayList<AlbumFile?>) {
                    /*mMimeType = video/mp4
                    mPath = /storage/emulated/0/Movies/20200909_164319511_703ae065b4ce9cf6eb4ead01bb12fbf4.mp4
                    mThumbPath = /storage/emulated/0/AlbumCache/2aeeb020e628d4de37f8c3b6dd7a93e5.album*/

                    path = result[0]?.thumbPath
                    mVideo = result[0]?.path

                    Glide.with(this@AddConditionsActivity)
                        .asBitmap()
                        .load(path)
                        .placeholder(R.drawable.ic_add_media)
                        .centerCrop()
                        .into(tvAddPicture)

                    mMediaType = getString(R.string.video)
                    ivPlay.visibility = View.VISIBLE

                }
            })
            .onCancel(object : Action<String?> {
                override fun onAction(result: String) {
                    logInfo(TAG, "Cancel: ${result}")
                }
            })
            .start()
    }

    private fun removeMedia() {
        if (mMediaType.equals(getString(R.string.image))) {
            uri = null
        } else if (mMediaType.equals(getString(R.string.video))) {
            path = null
            mVideo = null
        }
        Glide.with(this)
            .asBitmap()
            .load(R.drawable.ic_add_media)
            .placeholder(R.drawable.ic_add_media)
            .centerCrop()
            .into(tvAddPicture)

        mMediaType = ""
        ivPlay.visibility = View.GONE

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}