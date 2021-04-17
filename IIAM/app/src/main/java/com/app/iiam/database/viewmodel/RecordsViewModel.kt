package com.app.iiam.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.iiam.database.AppDatabase
import com.app.iiam.database.entities.Records
import com.app.iiam.database.repository.RecordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecordsRepository

    init {
        val recordsDao = AppDatabase.getDatabase(application, viewModelScope).recordsDao()
        repository = RecordsRepository(recordsDao)
    }

    fun insert(records: Records) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(records)
    }

    fun getAllRecords(userId: Long): LiveData<List<Records>> {
        return repository.getAllRecords(userId)
    }

    fun getRecords(recordsId: Long): LiveData<Records> {
        return repository.getRecords(recordsId)
    }

    fun updateRecords(
        userId: Long,
        recordsPicture: String,
        recordsDocumentsName: String,
        recordsDate: Long,
        recordsPagesInDocument: String,
        recordsId: Long
    ) {
        repository.updateRecords(userId, recordsPicture, recordsDocumentsName, recordsDate, recordsPagesInDocument, recordsId)
    }

    fun deleteRecords(recordsId: Long) {
        repository.deleteRecords(recordsId)
    }

    fun deleteAllRecords(userId: Long) {
        repository.deleteAllRecords(userId)
    }

}