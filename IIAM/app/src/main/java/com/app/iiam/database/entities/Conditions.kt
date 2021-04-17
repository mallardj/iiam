package com.app.iiam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conditions_table")
data class Conditions(

    @ColumnInfo(name = "user_id")
    var userId: Long?,

    @ColumnInfo(name = "conditions_media_type")
    val conditionsMediaType: String?,

    @ColumnInfo(name = "conditions_picture")
    val conditionsPicture: String?,

    @ColumnInfo(name = "conditions_video")
    val conditionsVideo: String?,

    @ColumnInfo(name = "conditions_name")
    val conditionsName: String?,

    @ColumnInfo(name = "conditions_symptoms")
    val conditionsSymptoms: String?,

    @ColumnInfo(name = "conditions_management")
    val conditionsManagement: String?,

    @ColumnInfo(name = "conditions_additional_comments")
    val conditionsAdditionalComments: String?

) {

    @PrimaryKey(autoGenerate = true)
    var conditionsId: Long = 0

}