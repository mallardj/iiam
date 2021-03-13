package com.app.iiam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(

    @ColumnInfo(name = "user_email")
    val userEmail: String?,

    @ColumnInfo(name = "user_password")
    val userPassword: String?

) {

    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0

}