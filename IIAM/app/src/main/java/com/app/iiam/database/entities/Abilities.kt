package com.app.iiam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "abilities_table")
data class Abilities(

    @ColumnInfo(name = "user_id")
    var userId: Long?,

    @ColumnInfo(name = "abilities_media_type")
    val abilitiesMediaType: String?,

    @ColumnInfo(name = "abilities_picture")
    val abilitiesPicture: String?,

    @ColumnInfo(name = "abilities_video")
    val abilitiesVideo: String?,

    @ColumnInfo(name = "abilities_ability")
    val abilitiesAbility: String?,

    @ColumnInfo(name = "abilities_task")
    val abilitiesTask: String?,

    @ColumnInfo(name = "abilities_comment")
    val abilitiesComment: String?

) {

    @PrimaryKey(autoGenerate = true)
    var abilitiesId: Long = 0

}