package com.app.iiam.addmedications

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.viewmodel.MedicationsViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_add_medications.*

class AutofillActivity : BaseActivity() {
    private lateinit var medicationsViewModel: MedicationsViewModel
    private lateinit var listView : ListView
    private var uri : Uri? = null
    private val detectedFields : ArrayList<String>? = ArrayList<String>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autofill_medications)

        initView()
        handleClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        medicationsViewModel = ViewModelProvider(this).get(MedicationsViewModel::class.java)
        val detectedFields = intent.getSerializableExtra("mylist") as ArrayList<String>
        print(detectedFields.size)
        uri = DetectedFields.uri
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.ic_add_picture)
            .centerCrop()
            .into(tvAddPicture)
        listView = findViewById<ListView>(R.id.detected_list_view)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, detectedFields)
        listView.adapter = adapter
        print("*******************************************")
        for (i in detectedFields) {
            print("I see something!")
            print(i)
        }

    }

    private fun handleClickListener() {

    }
}