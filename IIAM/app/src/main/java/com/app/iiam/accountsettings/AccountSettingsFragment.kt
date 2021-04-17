package com.app.iiam.accountsettings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.base.BaseFragment
import com.app.iiam.changepassword.ChangePasswordActivity
import com.app.iiam.database.viewmodel.*
import com.app.iiam.main.MainActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import com.app.iiam.utils.NetworkUtils
import com.app.iiam.welcome.WelcomeActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.item_alert_dialog.view.*

class AccountSettingsFragment : BaseFragment() {

    private var mUserId: Long = 0

    private lateinit var userViewModel: UserViewModel

    private lateinit var userDetailsViewModel: UserDetailsViewModel

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var abilitiesViewModel: AbilitiesViewModel

    private lateinit var medicationsViewModel: MedicationsViewModel

    private lateinit var conditionsViewModel: ConditionsViewModel

    private lateinit var recordsViewModel: RecordsViewModel

    private lateinit var mAuth: FirebaseAuth

    companion object {
        fun newInstance(context: Context): AccountSettingsFragment {
            val bundle = Bundle()
            val fragment = AccountSettingsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleClickListner()
    }

    private fun initView() {

        mAuth = FirebaseAuth.getInstance()

        mUserId = SharedPrefsManager.getLong(Const.USER_ID)

    }

    private fun handleClickListner() {

        llChangePassword.setOnClickListener {
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
            (activity as MainActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        llDeleteAccountInformation.setOnClickListener {
            if (!NetworkUtils.isConnected) {
                showError(getString(R.string.network_error))
            } else {
                showDeleteAccountDialog()
            }
        }

    }

    private fun showDeleteAccountDialog() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.item_alert_dialog, null)
        val mBuilder = AlertDialog.Builder(activity, R.style.CustomBottomSheetDialogTheme).setView(mDialogView).setCancelable(false)
        val mAlertDialog = mBuilder.show()
        mDialogView.tvDialogTitle.text = getString(R.string.are_you_sure_you_want_to_delete_this_account)
        mDialogView.tvDialogCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
        mDialogView.tvDialogDelete.setOnClickListener {
            deleteUser()
            mAlertDialog.dismiss()
        }
    }

    private fun deleteUser() {
        showLoader()

        mAuth.signInWithEmailAndPassword(SharedPrefsManager.getString(Const.USER_EMAIL), SharedPrefsManager.getString(Const.USER_PASSWORD)).addOnCompleteListener(activity!!) { tasksignin ->
            if (!tasksignin.isSuccessful) {
                hideLoader()
                if ((tasksignin.exception as FirebaseAuthInvalidUserException).errorCode.equals(getString(R.string.error_user_not_found))) {
                    deleteAccount()
                } else {
                    showError(tasksignin.exception!!.message)
                }
            } else {
                val mFirebaseUser = mAuth.currentUser
                if (mFirebaseUser != null) {
                    mFirebaseUser.delete().addOnCompleteListener(OnCompleteListener<Void?> { task ->
                        if (task.isSuccessful) {
                            deleteAccount()
                        } else {
                            hideLoader()
                            showError(task.exception!!.message)
                        }
                    })
                } else {
                    hideLoader()
                    showError(getString(R.string.we_cant_find_requested_user))
                }
            }
        }
    }

    private fun deleteAccount() {
        //showLoader()

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        abilitiesViewModel = ViewModelProvider(this).get(AbilitiesViewModel::class.java)
        medicationsViewModel = ViewModelProvider(this).get(MedicationsViewModel::class.java)
        conditionsViewModel = ViewModelProvider(this).get(ConditionsViewModel::class.java)
        recordsViewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)
        userDetailsViewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val thread = Thread(Runnable {
            noteViewModel.deleteAllNotes(mUserId)
            abilitiesViewModel.deleteAllAbilities(mUserId)
            medicationsViewModel.deleteAllMedications(mUserId)
            conditionsViewModel.deleteAllConditions(mUserId)
            recordsViewModel.deleteAllRecords(mUserId)
            userDetailsViewModel.deleteAllUserDetails(mUserId)
            userViewModel.deleteUser(mUserId)

            SharedPrefsManager.clearPrefs()
        })
        thread.start()

        hideLoader()

        val intent = Intent(activity, WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        (activity as MainActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}