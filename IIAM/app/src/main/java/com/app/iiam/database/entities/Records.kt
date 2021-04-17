package com.app.iiam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records_table")
data class Records(

    @ColumnInfo(name = "user_id")
    var userId: Long?,

    @ColumnInfo(name = "records_picture")
    val recordsPicture: String?,

    @ColumnInfo(name = "records_documents_name")
    val recordsDocumentsName: String?,

    @ColumnInfo(name = "records_date")
    val recordsDate: Long?,

    @ColumnInfo(name = "records_pages_in_document")
    val recordsPagesInDocument: String?

) {

    @PrimaryKey(autoGenerate = true)
    var recordsId: Long = 0

}