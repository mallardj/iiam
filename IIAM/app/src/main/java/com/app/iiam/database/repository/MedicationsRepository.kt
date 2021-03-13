package com.app.iiam.database.repository

import androidx.lifecycle.LiveData
import com.app.iiam.database.daos.MedicationsDao
import com.app.iiam.database.entities.Medications

class MedicationsRepository(private val medicationsDao: MedicationsDao) {

    fun insert(medications: Medications) {
        medicationsDao.insert(medications)
    }

    fun getAllMedications(userId: Long): LiveData<List<Medications>> {
        return medicationsDao.getAllMedications(userId)
    }

    fun getMedications(medicationsId: Long): LiveData<Medications> {
        return medicationsDao.getMedications(medicationsId)
    }

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
    ) {
        medicationsDao.updateMedications(
            userId,
            medicationsPicture,
            medicationsName,
            medicationsDose,
            medicationsDoesUnit,
            medicationsDoseInfo,
            medicationsRoute,
            medicationsRouteInfo,
            medicationsFrequency,
            medicationsId
        )
    }

    fun deleteMedications(medicationsId: Long) {
        medicationsDao.deleteMedications(medicationsId)
    }

    fun deleteAllMedications(userId: Long) {
        medicationsDao.deleteAllMedications(userId)
    }
}