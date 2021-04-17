package com.app.iiam.database.repository

import androidx.lifecycle.LiveData
import com.app.iiam.database.daos.AbilitiesDao
import com.app.iiam.database.entities.Abilities

class AbilitiesRepository(private val abilitiesDao: AbilitiesDao) {

    fun insert(abilities: Abilities) {
        abilitiesDao.insert(abilities)
    }

    fun getAllAbilities(userId: Long): LiveData<List<Abilities>> {
        return abilitiesDao.getAllAbilities(userId)
    }

    fun getAbilities(abilitiesId: Long): LiveData<Abilities> {
        return abilitiesDao.getAbilities(abilitiesId)
    }

    fun updateAbilities(
        userId: Long,
        abilitiesMediaType: String,
        abilitiesPicture: String,
        abilitiesVideo: String,
        abilitiesAbility: String,
        abilitiesTask: String,
        abilitiesComment: String,
        abilitiesId: Long
    ) {
        abilitiesDao.updateAbilities(userId, abilitiesMediaType, abilitiesPicture, abilitiesVideo, abilitiesAbility, abilitiesTask, abilitiesComment, abilitiesId)
    }

    fun deleteAbilities(abilitiesId: Long) {
        abilitiesDao.deleteAbilities(abilitiesId)
    }

    fun deleteAllAbilities(userId: Long) {
        abilitiesDao.deleteAllAbilities(userId)
    }
}