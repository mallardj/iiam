package com.app.iiam.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.iiam.database.entities.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)

    @Query("SELECT * from note_table WHERE user_id = (:userId)")
    fun getAllNotes(userId: Long): LiveData<List<Note>>

    @Query("SELECT * from note_table WHERE noteId = (:noteId)")
    fun getNote(noteId: Long): LiveData<Note>

    @Query("UPDATE note_table SET user_id = (:userId), note_media_type = (:noteMediaType), note_picture = (:notePicture), note_video = (:noteVideo), note_date = (:noteDate), note_title = (:noteTitle), note_comment = (:noteComment) WHERE noteId = (:noteId)")
    fun updateNote(
        userId: Long,
        noteMediaType: String,
        notePicture: String,
        noteVideo: String,
        noteDate: Long,
        noteTitle: String,
        noteComment: String,
        noteId: Long
    )

    @Query("DELETE FROM note_table WHERE noteId = (:noteId)")
    fun deleteNote(noteId: Long)

    @Query("DELETE FROM note_table WHERE user_id = (:userId)")
    fun deleteAllNotes(userId: Long)

}