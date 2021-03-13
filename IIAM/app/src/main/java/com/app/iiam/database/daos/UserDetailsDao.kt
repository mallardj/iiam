package com.app.iiam.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.iiam.database.entities.UserDetails

@Dao
interface UserDetailsDao {

    @Query("SELECT * from user_details_table")
    fun getUserDetailsData(): LiveData<List<UserDetails>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userDetails: UserDetails)

    @Query("SELECT * from user_details_table WHERE user_id = (:userId)")
    fun getUserDetails(userId: Long): LiveData<UserDetails>

    @Query("UPDATE user_details_table SET user_name = (:userName), user_preferred_name = (:userPreferredName), user_age = (:userAge), user_gender = (:userGender), user_allergies = (:userAllergies), user_phone_number = (:userPhoneNumber), user_emergency_contact = (:userEmergencyContact), user_profile_picture = (:userProfilePicture) WHERE user_id = (:userId)")
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
    )

    @Query("DELETE FROM user_details_table WHERE user_id = (:userId)")
    fun deleteAllUserDetails(userId: Long)
}