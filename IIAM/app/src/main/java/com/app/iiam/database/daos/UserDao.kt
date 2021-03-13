package com.app.iiam.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.iiam.database.entities.User

@Dao
interface UserDao {

    @Query("SELECT * from user_table")
    fun getUserData(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Query("DELETE FROM user_table")
    fun deleteAll()

    @Query("SELECT * from user_table WHERE userId = (:userId)")
    fun getUserPassword(userId: Long): LiveData<User>

    @Query("UPDATE user_table SET user_email = (:userEmail), user_password = (:userPassword) WHERE userId = (:userId)")
    fun updatePassword(
        userEmail: String,
        userPassword: String,
        userId: Long
    )

    @Query("DELETE FROM user_table WHERE userId = (:userId)")
    fun deleteUser(userId: Long)

    @Query("SELECT userId from user_table WHERE user_email = (:userEmail)")
    fun getUserId(userEmail: String): Long

}