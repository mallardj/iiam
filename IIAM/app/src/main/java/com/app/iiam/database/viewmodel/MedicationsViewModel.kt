package com.app.iiam.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.iiam.database.AppDatabase
import com.app.iiam.database.entities.Medications
import com.app.iiam.database.repository.MedicationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MedicationsRepository

    init {
        val medicationsDao = AppDatabase.getDatabase(application, viewModelScope).medicationsDao()
        repository = MedicationsRepository(medicationsDao)
    }

    fun insert(medications: Medications) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(medications)
    }

    fun getAllMedications(userId: Long): LiveData<List<Medications>> {
        return repository.getAllMedications(userId)
    }

    fun getMedications(medicationsId: Long): LiveData<Medications> {
        return repository.getMedications(medicationsId)
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
        repository.updateMedications(
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
        repository.deleteMedications(medicationsId)
    }

    fun deleteAllMedications(userId: Long) {
        repository.deleteAllMedications(userId)
    }

}