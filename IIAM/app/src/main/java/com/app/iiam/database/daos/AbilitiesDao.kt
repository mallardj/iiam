package com.app.iiam.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.iiam.database.entities.Abilities

@Dao
interface AbilitiesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(abilities: Abilities)

    @Query("SELECT * from abilities_table WHERE user_id = (:userId)")
    fun getAllAbilities(userId: Long): LiveData<List<Abilities>>

    @Query("SELECT * from abilities_table WHERE abilitiesId = (:abilitiesId)")
    fun getAbilities(abilitiesId: Long): LiveData<Abilities>

    @Query("UPDATE abilities_table SET user_id = (:userId), abilities_media_type = (:abilitiesMediaType), abilities_picture = (:abilitiesPicture), abilities_video = (:abilitiesVideo), abilities_ability = (:abilitiesAbility), abilities_task = (:abilitiesTask), abilities_comment = (:abilitiesComment) WHERE abilitiesId = (:abilitiesId)")
    fun updateAbilities(
        userId: Long,
        abilitiesMediaType: String,
        abilitiesPicture: String,
        abilitiesVideo: String,
        abilitiesAbility: String,
        abilitiesTask: String,
        abilitiesComment: String,
        abilitiesId: Long
    )

    @Query("DELETE FROM abilities_table WHERE abilitiesId = (:abilitiesId)")
    fun deleteAbilities(abilitiesId: Long)

    @Query("DELETE FROM abilities_table WHERE user_id = (:userId)")
    fun deleteAllAbilities(userId: Long)

}