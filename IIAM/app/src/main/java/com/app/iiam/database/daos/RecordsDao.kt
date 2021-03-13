package com.app.iiam.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.iiam.database.entities.Records

@Dao
interface RecordsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(records: Records)

    @Query("SELECT * from records_table WHERE user_id = (:userId)")
    fun getAllRecords(userId: Long): LiveData<List<Records>>

    @Query("SELECT * from records_table WHERE recordsId = (:recordsId)")
    fun getRecords(recordsId: Long): LiveData<Records>

    @Query("UPDATE records_table SET user_id = (:userId), records_picture = (:recordsPicture), records_documents_name = (:recordsDocumentsName), records_date = (:recordsDate), records_pages_in_document = (:recordsPagesInDocument) WHERE recordsId = (:recordsId)")
    fun updateRecords(
        userId: Long,
        recordsPicture: String,
        recordsDocumentsName: String,
        recordsDate: Long,
        recordsPagesInDocument:String,
        recordsId: Long
    )

    @Query("DELETE FROM records_table WHERE recordsId = (:recordsId)")
    fun deleteRecords(recordsId: Long)

    @Query("DELETE FROM records_table WHERE user_id = (:userId)")
    fun deleteAllRecords(userId: Long)

}