package com.dmribeiro.marvelinfinityapp.di

import android.content.Context
import androidx.room.Room
import com.dmribeiro.marvelinfinityapp.database.MovieDatabase
import com.dmribeiro.marvelinfinityapp.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
       context,
       MovieDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesDao(database: MovieDatabase) = database.mealsDao()


}