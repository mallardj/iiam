package com.app.iiam.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.iiam.database.AppDatabase
import com.app.iiam.database.entities.Conditions
import com.app.iiam.database.repository.ConditionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConditionsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ConditionsRepository

    init {
        val conditionsDao = AppDatabase.getDatabase(application, viewModelScope).conditionsDao()
        repository = ConditionsRepository(conditionsDao)
    }

    fun insert(conditions: Conditions) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(conditions)
    }

    fun getAllConditions(userId: Long): LiveData<List<Conditions>> {
        return repository.getAllConditions(userId)
    }

    fun getConditions(conditionsId: Long): LiveData<Conditions> {
        return repository.getConditions(conditionsId)
    }

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
    ) {
        repository.updateConditions(
            userId,
            conditionsMediaType,
            conditionsPicture,
            conditionsVideo,
            conditionsName,
            conditionsSymptoms,
            conditionsManagement,
            conditionsAdditionalComments,
            conditionsId
        )
    }

    fun deleteConditions(conditionsId: Long) {
        repository.deleteConditions(conditionsId)
    }

    fun deleteAllConditions(userId: Long) {
        repository.deleteAllConditions(userId)
    }

}