package com.app.iiam.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.iiam.database.AppDatabase
import com.app.iiam.database.entities.User
import com.app.iiam.database.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allUsers: LiveData<List<User>>

    init {
        val userDao = AppDatabase.getDatabase(application, viewModelScope).userDao()
        repository = UserRepository(userDao)
        allUsers = repository.allUsers
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }

    fun getUserPassword(userId: Long): LiveData<User> {
        return repository.getUserPassword(userId)
    }

    fun updatePassword(
        userEmail: String,
        userPassword: String,
        userId: Long
    ) {
        repository.updatePassword(userEmail, userPassword, userId)
    }

    fun deleteUser(userId: Long) {
        repository.deleteUser(userId)
    }

    fun getUserId(userEmail: String): Long {
        return repository.getUserId(userEmail)
    }

}