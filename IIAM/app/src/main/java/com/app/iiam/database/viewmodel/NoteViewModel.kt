package com.app.iiam.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.iiam.database.AppDatabase
import com.app.iiam.database.entities.Note
import com.app.iiam.database.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    init {
        val noteDao = AppDatabase.getDatabase(application, viewModelScope).noteDao()
        repository = NoteRepository(noteDao)
    }

    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun getAllNotes(userId: Long): LiveData<List<Note>> {
        return repository.getAllNotes(userId)
    }

    fun getNote(noteId: Long): LiveData<Note> {
        return repository.getNote(noteId)
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
        repository.updateNote(userId, noteMediaType, notePicture, noteVideo, noteDate, noteTitle, noteComment, noteId)
    }

    fun deleteNote(noteId: Long) {
        repository.deleteNote(noteId)
    }

    fun deleteAllNotes(userId: Long) {
        repository.deleteAllNotes(userId)
    }

}