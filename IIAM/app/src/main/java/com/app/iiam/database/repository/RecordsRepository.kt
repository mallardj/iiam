package com.app.iiam.database.repository

import androidx.lifecycle.LiveData
import com.app.iiam.database.daos.RecordsDao
import com.app.iiam.database.entities.Records

class RecordsRepository(private val recordsDao: RecordsDao) {

    fun insert(records: Records) {
        recordsDao.insert(records)
    }

    fun getAllRecords(userId: Long): LiveData<List<Records>> {
        return recordsDao.getAllRecords(userId)
    }

    fun getRecords(recordsId: Long): LiveData<Records> {
        return recordsDao.getRecords(recordsId)
    }

    fun updateRecords(
        userId: Long,
        recordsPicture: String,
        recordsDocumentsName: String,
        recordsDate: Long,
        recordsPagesInDocument: String,
        recordsId: Long
    ) {
        recordsDao.updateRecords(userId, recordsPicture, recordsDocumentsName, recordsDate, recordsPagesInDocument, recordsId)
    }

    fun deleteRecords(recordsId: Long) {
        recordsDao.deleteRecords(recordsId)
    }

    fun deleteAllRecords(userId: Long) {
        recordsDao.deleteAllRecords(userId)
    }
}