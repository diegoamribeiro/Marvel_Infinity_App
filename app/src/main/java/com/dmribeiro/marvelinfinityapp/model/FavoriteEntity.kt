package com.dmribeiro.marvelinfinityapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dmribeiro.marvelinfinityapp.utils.Constants.FAVORITES_TABLE

@Entity(tableName = FAVORITES_TABLE)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val movie: MovieItem
)
