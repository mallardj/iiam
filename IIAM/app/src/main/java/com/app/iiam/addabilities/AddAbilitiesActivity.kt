package com.app.iiam.addabilities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.adapter.SpinnerArrayAdapter
import com.app.iiam.addnote.MediaBottomSheetFragment
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.Abilities
import com.app.iiam.database.viewmodel.AbilitiesViewModel
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import com.app.iiam.utils.logInfo
import com.bumptech.glide.Glide
import com.yanzhenjie.album.Action
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import kotlinx.android.synthetic.main.activity_add_abilities.*
import kotlinx.android.synthetic.main.toolbar_home.*
import lv.chi.photopicker.PhotoPickerFragment
import java.util.*

class AddAbilitiesActivity : BaseActivity(), PhotoPickerFragment.Callback, MediaBottomSheetFragment.ItemClickListener {

    private lateinit var abilitiesViewModel: AbilitiesViewModel
    private var uri: Uri? = null
    private var mTaskList = mutableListOf<String>()
    private var isEditAbility = false
    private var mAbilityId: Long = 0
    private var mVideo: String? = null
    private var path: String? = null
    private var mMediaType = ""

    companion object {
        private const val TAG = "AddAbilitiesActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_abilities)

        initView()
        handleClickListner()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        abilitiesViewModel = ViewModelProvider(this).get(AbilitiesViewModel::class.java)

        isEditAbility = intent.getBooleanExtra(Const.IS_EDIT_ABILITY, false)

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.visibility = View.VISIBLE

        if (isEditAbility) {
            mAbilityId = intent.getLongExtra(Const.ID_ABILITY, 0)
            tvToolbarName.setText(getString(R.string.edit_abilities))
            getAbilityDetailsFromDB()
        } else {
            tvToolbarName.setText(getString(R.string.add_abilities_))
        }

        mTaskList.add(getString(R.string.select_task))
        mTaskList.add(getString(R.string.can_independently_perform_task))
        mTaskList.add(getString(R.string.can_partly_perform_task))
        mTaskList.add(getString(R.string.needs_help_performing_task))
        spTask.adapter = SpinnerArrayAdapter(this, mTaskList)

        etComment.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etComment.hasFocus()) {
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

    private fun getAbilityDetailsFromDB() {
        showLoader()

        abilitiesViewModel.getAbilities(mAbilityId).observe(this, Observer { ability ->
            ability.let {

                if (it.abilitiesMediaType.equals(getString(R.string.image))) {
                    mMediaType = getString(R.string.image)
                    uri = Uri.parse(it.abilitiesPicture)
                    Glide.with(this)
                        .asBitmap()
                        .load(uri)
                        .placeholder(R.drawable.ic_add_media)
                        .centerCrop()
                        .into(tvAddPicture)

                    ivPlay.visibility = View.GONE
                } else if (it.abilitiesMediaType.equals(getString(R.string.video))) {
                    mMediaType = getString(R.string.video)
                    path = it.abilitiesPicture
                    mVideo = it.abilitiesVideo
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

                etAbility.setText(it.abilitiesAbility)

                if (it.abilitiesTask.equals(getString(R.string.can_independently_perform_task))) {
                    spTask.setSelection(1)
                } else if (it.abilitiesTask.equals(getString(R.string.can_partly_perform_task))) {
                    spTask.setSelection(2)
                } else if (it.abilitiesTask.equals(getString(R.string.needs_help_performing_task))) {
                    spTask.setSelection(3)
                }

                etComment.setText(it.abilitiesComment)

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
            if (TextUtils.isEmpty(etAbility.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.ability).toLowerCase()
                    )
                )
            } else if (etAbility.text.toString().trim().length < 2) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.ability),
                            resources.getInteger(R.integer.min_name)
                        )
                    )
                )
            } else if (spTask.selectedItemPosition == 0) {
                showError(
                    String.format(
                        getString(R.string.validation_selection),
                        getString(R.string.task).toLowerCase()
                    )
                )
            } else if (TextUtils.isEmpty(etComment.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.comments).toLowerCase()
                    )
                )
            } else if (etComment.text.toString().trim().length < 2) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.comments),
                            resources.getInteger(R.integer.min_name)
                        )
                    )
                )
            } else {
                if (mMediaType.equals(getString(R.string.image))) {
                    showLoader()
                    if (isEditAbility) {
                        val thread = Thread(Runnable {
                            abilitiesViewModel.updateAbilities(
                                SharedPrefsManager.getLong(Const.USER_ID),
                                mMediaType,
                                if (uri != null) uri.toString() else "",
                                "",
                                etAbility.text.toString().trim(),
                                mTaskList[spTask.selectedItemPosition],
                                etComment.text.toString().trim(),
                                mAbilityId
                            )

                            hideLoader()
                            finish()
                        })
                        thread.start()
                    } else {
                        val abilities = Abilities(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            mMediaType,
                            if (uri != null) uri.toString() else "",
                            "",
                            etAbility.text.toString().trim(),
                            mTaskList[spTask.selectedItemPosition],
                            etComment.text.toString().trim()
                        )
                        abilitiesViewModel.insert(abilities)

                        hideLoader()
                        finish()
                    }
                } else if (mMediaType.equals(getString(R.string.video))) {
                    showLoader()
                    if (isEditAbility) {
                        val thread = Thread(Runnable {
                            abilitiesViewModel.updateAbilities(
                                SharedPrefsManager.getLong(Const.USER_ID),
                                mMediaType,
                                if (path != null) path.toString() else "",
                                if (mVideo != null) mVideo.toString() else "",
                                etAbility.text.toString().trim(),
                                mTaskList[spTask.selectedItemPosition],
                                etComment.text.toString().trim(),
                                mAbilityId
                            )

                            hideLoader()
                            finish()
                        })
                        thread.start()
                    } else {
                        val abilities = Abilities(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            mMediaType,
                            if (path != null) path.toString() else "",
                            if (mVideo != null) mVideo.toString() else "",
                            etAbility.text.toString().trim(),
                            mTaskList[spTask.selectedItemPosition],
                            etComment.text.toString().trim()
                        )
                        abilitiesViewModel.insert(abilities)

                        hideLoader()
                        finish()
                    }
                } else {
                    showLoader()
                    if (isEditAbility) {
                        val thread = Thread(Runnable {
                            abilitiesViewModel.updateAbilities(
                                SharedPrefsManager.getLong(Const.USER_ID),
                                mMediaType,
                                "",
                                "",
                                etAbility.text.toString().trim(),
                                mTaskList[spTask.selectedItemPosition],
                                etComment.text.toString().trim(),
                                mAbilityId
                            )

                            hideLoader()
                            finish()
                        })
                        thread.start()
                    } else {
                        val abilities = Abilities(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            mMediaType,
                            "",
                            "",
                            etAbility.text.toString().trim(),
                            mTaskList[spTask.selectedItemPosition],
                            etComment.text.toString().trim()
                        )
                        abilitiesViewModel.insert(abilities)

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

                    Glide.with(this@AddAbilitiesActivity)
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