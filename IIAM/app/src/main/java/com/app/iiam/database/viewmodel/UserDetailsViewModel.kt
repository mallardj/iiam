package com.app.iiam.database.viewmodel

import android.app.Application
import android.provider.LiveFolders
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.iiam.database.AppDatabase
import com.app.iiam.database.entities.UserDetails
import com.app.iiam.database.repository.UserDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserDetailsRepository

    val allUsersDetails: LiveData<List<UserDetails>>

    init {
        val userDetailsDao = AppDatabase.getDatabase(application, viewModelScope).userDetailsDao()
        repository = UserDetailsRepository(userDetailsDao)

        allUsersDetails = repository.allUsersDetails
    }

    fun insert(userDetails: UserDetails) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(userDetails)
    }

    fun getUserDetails(userId: Long): LiveData<UserDetails> {
        return repository.getUserDetails(userId)
    }

    fun updateUserDetails(
        userId: Long,
        userName: String,
        userPreferredName: String,
        userAge: String,
        userGender: String,
        userAllergies: String,
        userPhoneNumber: String,
        userEmergencyContact: String,
        userProfilePicture: String
    ) {
        repository.updateUserDetails(userId, userName, userPreferredName, userAge, userGender, userAllergies, userPhoneNumber, userEmergencyContact, userProfilePicture)
    }

    fun deleteAllUserDetails(userId: Long) {
        repository.deleteAllUserDetails(userId)
    }

}