package com.app.iiam.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.iiam.database.AppDatabase
import com.app.iiam.database.entities.Abilities
import com.app.iiam.database.repository.AbilitiesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AbilitiesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AbilitiesRepository

    init {
        val abilitiesDao = AppDatabase.getDatabase(application, viewModelScope).abilitiesDao()
        repository = AbilitiesRepository(abilitiesDao)
    }

    fun insert(abilities: Abilities) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(abilities)
    }

    fun getAllAbilities(userId: Long): LiveData<List<Abilities>> {
        return repository.getAllAbilities(userId)
    }

    fun getAbilities(abilitiesId: Long): LiveData<Abilities> {
        return repository.getAbilities(abilitiesId)
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
        repository.updateAbilities(userId, abilitiesMediaType, abilitiesPicture, abilitiesVideo, abilitiesAbility, abilitiesTask, abilitiesComment, abilitiesId)
    }

    fun deleteAbilities(abilitiesId: Long) {
        repository.deleteAbilities(abilitiesId)
    }

    fun deleteAllAbilities(userId: Long) {
        repository.deleteAllAbilities(userId)
    }

}