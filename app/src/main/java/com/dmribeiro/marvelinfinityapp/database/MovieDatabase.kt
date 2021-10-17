package com.dmribeiro.marvelinfinityapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dmribeiro.marvelinfinityapp.model.FavoriteEntity
import com.dmribeiro.marvelinfinityapp.model.MovieItem

@Database(
    entities = [MovieItem::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun mealsDao(): MovieDao

}