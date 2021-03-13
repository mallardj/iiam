package com.app.iiam.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.iiam.database.entities.Medications

@Dao
interface MedicationsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(medications: Medications)

    @Query("SELECT * from medications_table WHERE user_id = (:userId)")
    fun getAllMedications(userId: Long): LiveData<List<Medications>>

    @Query("SELECT * from medications_table WHERE medicationsId = (:medicationsId)")
    fun getMedications(medicationsId: Long): LiveData<Medications>

    @Query("UPDATE medications_table SET user_id = (:userId), medications_picture = (:medicationsPicture), medications_name = (:medicationsName), medications_dose = (:medicationsDose), medications_does_unit = (:medicationsDoesUnit), medications_dose_info = (:medicationsDoseInfo), medications_route = (:medicationsRoute), medications_route_info = (:medicationsRouteInfo), medications_frequency = (:medicationsFrequency) WHERE medicationsId = (:medicationsId)")
    fun updateMedications(
        userId: Long,
        medicationsPicture: String,
        medicationsName: String,
        medicationsDose: String,
        medicationsDoesUnit: String,
        medicationsDoseInfo: String,
        medicationsRoute: String,
        medicationsRouteInfo: String,
        medicationsFrequency: String,
        medicationsId: Long
    )

    @Query("DELETE FROM medications_table WHERE medicationsId = (:medicationsId)")
    fun deleteMedications(medicationsId: Long)

    @Query("DELETE FROM medications_table WHERE user_id = (:userId)")
    fun deleteAllMedications(userId: Long)

}