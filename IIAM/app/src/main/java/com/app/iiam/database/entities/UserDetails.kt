package com.app.iiam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_details_table")
class UserDetails(

    @ColumnInfo(name = "user_id")
    var userId: Long?,

    @ColumnInfo(name = "user_name")
    val userName: String?,

    @ColumnInfo(name = "user_preferred_name")
    val userPreferredName: String?,

    @ColumnInfo(name = "user_age")
    val userAge: String?,

    @ColumnInfo(name = "user_gender")
    val userGender: String?,

    @ColumnInfo(name = "user_allergies")
    val userAllergies: String?,

    @ColumnInfo(name = "user_phone_number")
    val userPhoneNumber: String?,

    @ColumnInfo(name = "user_emergency_contact")
    val userEmergencyContact: String?,

    @ColumnInfo(name = "user_profile_picture")
    val userProfilePicture: String?

) {

    @PrimaryKey(autoGenerate = true)
    var userDetailsId: Long = 0

}