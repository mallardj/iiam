package com.app.iiam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(

    @ColumnInfo(name = "user_id")
    var userId: Long?,

    @ColumnInfo(name = "note_media_type")
    val noteMediaType: String?,

    @ColumnInfo(name = "note_picture")
    val notePicture: String?,

    @ColumnInfo(name = "note_video")
    val noteVideo: String?,

    @ColumnInfo(name = "note_date")
    val noteDate: Long?,

    @ColumnInfo(name = "note_title")
    val noteTitle: String?,

    @ColumnInfo(name = "note_comment")
    val noteComment: String?

) {

    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0

}