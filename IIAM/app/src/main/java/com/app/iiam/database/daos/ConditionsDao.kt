package com.app.iiam.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.iiam.database.entities.Conditions

@Dao
interface ConditionsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(conditions: Conditions)

    @Query("SELECT * from conditions_table WHERE user_id = (:userId)")
    fun getAllConditions(userId: Long): LiveData<List<Conditions>>

    @Query("SELECT * from conditions_table WHERE conditionsId = (:conditionsId)")
    fun getConditions(conditionsId: Long): LiveData<Conditions>

    @Query("UPDATE conditions_table SET user_id = (:userId), conditions_media_type = (:conditionsMediaType), conditions_picture = (:conditionsPicture), conditions_video = (:conditionsVideo), conditions_name = (:conditionsName), conditions_symptoms = (:conditionsSymptoms), conditions_management = (:conditionsManagement), conditions_additional_comments = (:conditionsAdditionalComments) WHERE conditionsId = (:conditionsId)")
    fun updateConditions(
        userId: Long,
        conditionsMediaType: String,
        conditionsPicture: String,
        conditionsVideo: String,
        conditionsName: String,
        conditionsSymptoms: String,
        conditionsManagement: String,
        conditionsAdditionalComments: String,
        conditionsId: Long
    )

    @Query("DELETE FROM conditions_table WHERE conditionsId = (:conditionsId)")
    fun deleteConditions(conditionsId: Long)

    @Query("DELETE FROM conditions_table WHERE user_id = (:userId)")
    fun deleteAllConditions(userId: Long)

}