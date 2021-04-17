package com.app.iiam.database.repository

import androidx.lifecycle.LiveData
import com.app.iiam.database.daos.ConditionsDao
import com.app.iiam.database.entities.Conditions

class ConditionsRepository(private val conditionsDao: ConditionsDao) {

    fun insert(conditions: Conditions) {
        conditionsDao.insert(conditions)
    }

    fun getAllConditions(userId: Long): LiveData<List<Conditions>> {
        return conditionsDao.getAllConditions(userId)
    }

    fun getConditions(conditionsId: Long): LiveData<Conditions> {
        return conditionsDao.getConditions(conditionsId)
    }

    fun updateConditions(
        userId: Long,
        conditionsMediaType: String,
        conditionsPicture: String,
        conditionsVideo: String,
        conditionsNme: String,
        conditionsSymptoms: String,
        conditionsManagement: String,
        conditionsAdditionalComments: String,
        conditionsId: Long
    ) {
        conditionsDao.updateConditions(
            userId,
            conditionsMediaType,
            conditionsPicture,
            conditionsVideo,
            conditionsNme,
            conditionsSymptoms,
            conditionsManagement,
            conditionsAdditionalComments,
            conditionsId
        )
    }

    fun deleteConditions(conditionsId: Long) {
        conditionsDao.deleteConditions(conditionsId)
    }

    fun deleteAllConditions(userId: Long) {
        conditionsDao.deleteAllConditions(userId)
    }
}