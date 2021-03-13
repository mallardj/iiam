package com.app.iiam.database.repository

import androidx.lifecycle.LiveData
import com.app.iiam.database.daos.UserDetailsDao
import com.app.iiam.database.entities.UserDetails

class UserDetailsRepository(private val userDetailsDao: UserDetailsDao) {

    val allUsersDetails: LiveData<List<UserDetails>> = userDetailsDao.getUserDetailsData()

    fun insert(userDetails: UserDetails) {
        userDetailsDao.insert(userDetails)
    }

    fun getUserDetails(userId: Long): LiveData<UserDetails> {
        return userDetailsDao.getUserDetails(userId)
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
        userDetailsDao.updateUserDetails(userId, userName, userPreferredName, userAge, userGender, userAllergies, userPhoneNumber, userEmergencyContact, userProfilePicture)
    }

    fun deleteAllUserDetails(userId: Long) {
        userDetailsDao.deleteAllUserDetails(userId)
    }
}