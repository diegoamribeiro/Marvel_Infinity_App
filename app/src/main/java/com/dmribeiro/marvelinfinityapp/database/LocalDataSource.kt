package com.dmribeiro.marvelinfinityapp.database

import androidx.lifecycle.LiveData
import com.dmribeiro.marvelinfinityapp.model.FavoriteEntity
import com.dmribeiro.marvelinfinityapp.model.MovieItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val movieDao: MovieDao
) {

    fun getAllMoviesFromDataBase(): Flow<List<MovieItem>>{
        return movieDao.readAllMovies()
    }

    fun searchFromDatabase(searchQuery: String): LiveData<List<MovieItem>>{
        return  movieDao.searchDatabase(searchQuery)
    }

    fun filterByTitle(): LiveData<List<MovieItem>>{
        return  movieDao.filterByTitle()
    }

    suspend fun insertMovies(movieItem: MovieItem){
        movieDao.insertMovie(movieItem)
    }

    suspend fun insertFavoriteMovie(favoriteEntity: FavoriteEntity){
        movieDao.insertFavoriteMovie(favoriteEntity)
    }

    fun readFavoritesMovies() : Flow<List<FavoriteEntity>>{
        return movieDao.readFavoritesMovie()
    }

    suspend fun deleteFavoriteMovie(favoriteEntity: FavoriteEntity){
        movieDao.deleteFavoriteMovie(favoriteEntity)
    }

    suspend fun deleteAllFavoritesMovies(){
        movieDao.deleteAllFavoritesMovies()
    }


}