package com.app.iiam.database.repository

import androidx.lifecycle.LiveData
import com.app.iiam.database.daos.UserDao
import com.app.iiam.database.entities.Abilities
import com.app.iiam.database.entities.User

class UserRepository(private val userDao: UserDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allUsers: LiveData<List<User>> = userDao.getUserData()

    fun insert(user: User) {
        userDao.insert(user)
    }

    fun getUserPassword(userId: Long): LiveData<User> {
        return userDao.getUserPassword(userId)
    }

    fun updatePassword(
        userEmail: String,
        userPassword: String,
        userId: Long
    ) {
        userDao.updatePassword(userEmail, userPassword, userId)
    }

    fun deleteUser(userId: Long){
        userDao.deleteUser(userId)
    }

    fun getUserId(userEmail: String): Long {
        return userDao.getUserId(userEmail)
    }
}