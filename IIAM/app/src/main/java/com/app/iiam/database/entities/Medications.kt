package com.app.iiam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications_table")
data class Medications(

    @ColumnInfo(name = "user_id")
    var userId: Long?,

    @ColumnInfo(name = "medications_picture")
    val medicationsPicture: String?,

    @ColumnInfo(name = "medications_name")
    val medicationsName: String?,

    @ColumnInfo(name = "medications_dose")
    val medicationsDose: String?,

    @ColumnInfo(name = "medications_does_unit")
    val medicationsDoseUnit: String?,

    @ColumnInfo(name = "medications_dose_info")
    val medicationsDoseInfo: String?,

    @ColumnInfo(name = "medications_route")
    val medicationsRoute: String?,

    @ColumnInfo(name = "medications_route_info")
    val medicationsRouteInfo: String?,

    @ColumnInfo(name = "medications_frequency")
    val medicationsFrequency: String?

) {

    @PrimaryKey(autoGenerate = true)
    var medicationsId: Long = 0

}