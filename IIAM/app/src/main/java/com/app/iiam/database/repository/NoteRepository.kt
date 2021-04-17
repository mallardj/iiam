package com.app.iiam.database.repository

import androidx.lifecycle.LiveData
import com.app.iiam.database.daos.NoteDao
import com.app.iiam.database.entities.Note

class NoteRepository(private val noteDao: NoteDao) {

    fun insert(note: Note) {
        noteDao.insert(note)
    }

    fun getAllNotes(userId: Long): LiveData<List<Note>> {
        return noteDao.getAllNotes(userId)
    }

    fun getNote(noteId: Long): LiveData<Note> {
        return noteDao.getNote(noteId)
    }

    fun updateNote(
        userId: Long,
        noteMediaType: String,
        notePicture: String,
        noteVideo: String,
        noteDate: Long,
        noteTitle: String,
        noteComment: String,
        noteId: Long
    ) {
        noteDao.updateNote(userId, noteMediaType, notePicture, noteVideo, noteDate, noteTitle, noteComment, noteId)
    }

    fun deleteNote(noteId: Long) {
        noteDao.deleteNote(noteId)
    }

    fun deleteAllNotes(userId: Long) {
        noteDao.deleteAllNotes(userId)
    }
}